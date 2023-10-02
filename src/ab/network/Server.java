package ab.network;

import ab.log.Log;
import ab.model.chat.Message;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static ab.model.chat.MessageType.*;

public class Server extends PrimaryNetworkUnit {
    ArrayList<ServerSocketHandler> handlers;
    final Map<String, Connection> connections = new ConcurrentHashMap<>();
    String serverName;
    String password = "";
    private boolean log;


    public Server(NetworkController networkController, String serverName, String password, boolean log) {
        super(networkController);
        this.serverName = serverName;
        this.password = password;
        initHandlers();
        this.log = log;
    }

    private void initHandlers() {
        handlers = new ArrayList<>();
        networkController.localNetworks.forEach(ia -> handlers.add(new ServerSocketHandler(ia)));
    }

    @Override
    void send(Message message) {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ITERATOR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (Map.Entry<String, Connection> c : connections.entrySet()) {
            try {
                c.getValue().send(message);
            } catch (IOException e) {
                networkController.messageController.add(new Message(CONNECTION_CLOSED, c.getKey()));
                c.getValue().close();
            }
        }
    }

    @Override
    void launch() {
        handlers.forEach(this::startTask);
    }

    class ServerSocketHandler implements Runnable {
        private final InterfaceAddress ia;

        ServerSocketHandler(InterfaceAddress ia) {
            this.ia = ia;
        }

        @Override
        public void run() {
            ServerBrdCell brdCell = null;
            try (ServerSocket socket = new ServerSocket(0, 30, ia.getAddress())) {
                if (log) Log.log("server socket is open: " + socket);
                registerResource(socket);
                brdCell = new ServerBrdCell(getReply(socket));
                startTask(brdCell);
                while (!Thread.currentThread().isInterrupted()) {
                    Socket newConnection = socket.accept();
                    if (log) Log.log(socket + " accepted connection: " + newConnection);
                    startTask(new ServerConnectionHandler(newConnection));
                }
            } catch (IOException ignore) {
            } finally {
                if (log) Log.log("server socket in finally");
                if (brdCell != null) brdCell.close();
            }

        }

        byte[] getReply(ServerSocket socket) throws IOException {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(socket.getInetAddress().getAddress());
            stream.write(new byte[]{(byte)(socket.getLocalPort()>>>8),(byte)socket.getLocalPort()});
            stream.write(serverName.getBytes());
            return stream.toByteArray();
        }

        class ServerBrdCell implements Runnable, Closeable {
            private DatagramSocket receiver;
            private DatagramSocket sender;
            private final byte[] reply;

            ServerBrdCell(byte[] reply) {
                this.reply = reply;
            }

            @Override
            public void run() {
                DatagramPacket packet = new DatagramPacket(new byte[0], 0);
                try (ServerBrdCell openedCell = open()) {
                    registerResource(openedCell);
                    if (log) Log.log("server broadcast is open: " + receiver);
                    while (! Thread.currentThread().isInterrupted()) {

                            receiver.receive(packet);
                            if (log) Log.log("server broadcast packet received: " + packet.getSocketAddress());
                            sender.send(new DatagramPacket(reply, reply.length, packet.getSocketAddress()));

                    }
                } catch (SocketException ignore) {
                    if (log) Log.log("server brd is closed in catch socket exception");
                } catch (IOException e) {
                    if (log) Log.log("server brd is closed in catch IOException");
                }
            }

            private ServerBrdCell open() throws SocketException {
                receiver = new DatagramSocket(19819, ia.getAddress());
                sender = getSender(ia);
                return this;
            }

            private DatagramSocket getSender(InterfaceAddress ia) {
                for (int port = 19820; port <= 65536; port++) {
                    try {
                        return new DatagramSocket(port, ia.getAddress());
                    } catch (SocketException ignore) {}
                }
                return null;
            }

            @Override
            public void close() {
                if (log) Log.log("Server Broadcast in close");
                if (receiver != null) receiver.close();
                if (sender != null) sender.close();
            }
        }

    }

    class ServerConnectionHandler implements Runnable {
        Socket socket;

        public ServerConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String userName = null;
            try (Connection connection = new Connection(socket)) {
                if (log) Log.log("server, new connection in progress: " + socket);
                if (serverIfPasswordCorrect(connection)) {
                    userName = serverHandshake(connection);
                    connections.put(userName, connection);
                    serverMainLoop(connection, userName);
                }
            } catch (IOException | ClassNotFoundException e) {
                //
            } finally {
                if (userName != null) {
                    connections.remove(userName);
                }
            }

        }

        private boolean serverIfPasswordCorrect(Connection connection) throws IOException, ClassNotFoundException {
            for (int i = 0; i < 6; i++) {
                connection.send(Message.PASSWORD_REQUEST);
                Message reply = connection.receive();
                if (reply == null || PASSWORD_REPLY != reply.getType() || reply.getData().isEmpty()) {
                    continue;
                }
                if (password.equals(reply.getData())) {
                    connection.send(Message.PASSWORD_ACCEPTED);
                    return true;
                }
            }
            connection.send(Message.CONNECTION_REJECTED);
            return false;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                Message reply = connection.receive();
                if (reply == null || USER_NAME != reply.getType() || reply.getData().isEmpty()) {
                    connection.send(Message.NAME_REQUEST);
                    continue;
                }
                if (connections.containsKey(reply.getData())) {
                    connection.send(new Message(NAME_REQUEST, reply.getData()));
                } else {
                    connection.send(Message.NAME_ACCEPTED);
                    return reply.getData();
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                networkController.messageController.add(connection.receive());
            }
        }
    }
}

package ab.network;

import ab.model.chat.Message;
import ab.network.exceptions.ConnectionError;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ab.model.chat.MessageType.*;

public class Server extends NetworkUnit {
    HashMap<IPWrapper, ConnectionBuilder> handlers;
    ArrayList<java.net.Socket> establishedConnections = new ArrayList<>();


    public Server(ConnectionManager connectionManager) throws ConnectionError {
        super(connectionManager);
        initHandlers();
        handlers.values().forEach(ConnectionBuilder::launch);
    }

    private void initHandlers() throws ConnectionError {
        for (Map.Entry<IPWrapper, InterfaceAddress> localNetwork : connectionManager.localNetworks.entrySet()) {
            try {
                handlers.put(localNetwork.getKey(), new ServerConnectionBuilder(localNetwork.getValue()));
            } catch (IOException ignore) {}
        }
        if (handlers.isEmpty()) throw new ConnectionError();
    }

    @Override
    public void close() {
        for (ConnectionBuilder connectionBuilder : handlers.values()) {
            try {
                connectionBuilder.close();
            } catch (IOException ignore) {}
        }
    }

    class ServerConnectionBuilder extends ConnectionBuilder {
        private final ServerSocket socket;
        private final ServerBrdListener brdListener;

        ServerConnectionBuilder(InterfaceAddress ia) throws IOException {
            socket = new ServerSocket(0, 20, ia.getAddress());
            try {
                brdListener = new ServerBrdListener(ia, getReply());
            } catch (IOException e) {
                socket.close();
                throw e;
            }
        }

        byte[] getReply() {
            byte[] ip = socket.getInetAddress().getAddress();
            return new byte[] {ip[0],ip[1],ip[2],ip[3],(byte)(socket.getLocalPort()>>>8),(byte)socket.getLocalPort()};
        }

        public void launch() {
            brdListener.start();
            start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    establishedConnections.add(socket.accept());
                } catch (IOException ignore) {}
            }
        }

        @Override
        public void close() throws IOException {
            if (brdListener != null) brdListener.close();
            if (socket != null) socket.close();
        }
    }

    class ServerBrdListener extends BrdListener {
        private final DatagramSocket receiver;
        private final DatagramSocket sender;
        private final byte[] reply;

        ServerBrdListener(InterfaceAddress ia, byte[] reply) throws IOException {
            receiver = new DatagramSocket(19819, ia.getAddress());
            sender = getSender(ia);
            this.reply = reply;
        }

        private DatagramSocket getSender(InterfaceAddress ia) throws ConnectionError {
            for (int port = 19820; port <= 65536; port++) {
                try {
                    return new DatagramSocket(port, ia.getAddress());
                } catch (SocketException ignore) {}
            }
            throw new ConnectionError();
        }

        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[0], 0);
            while(!isInterrupted()) {
                try {
                    receiver.receive(packet);
                    sender.send(new DatagramPacket(reply, 6, packet.getSocketAddress()));
                } catch (IOException ignore) {}
            }
            close();
        }

        @Override
        public void close() {
            if (receiver != null) receiver.close();
            if (sender != null) sender.close();
        }
    }

    class ConnectionHandler extends Handler {
        Socket socket;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String userName = null;
            try (Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);
                connectionManager.addConnection(userName, connection);
                serverMainLoop(connection, userName);
            } catch (IOException | ClassNotFoundException e) {
                //
            } finally {
                if (userName != null) {
                    connectionManager.removeConnection(userName);
                }
            }

        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(Message.NAME_REQUEST);
                Message reply = connection.receive();
                if (reply == null || USER_NAME != reply.getType() || reply.getData().isEmpty()) continue;
                if (connectionManager.connections.containsKey(reply.getData())) {
                    connection.send(new Message(NAME_REQUEST, reply.getData()));
                } else {
                    connection.send(Message.NAME_ACCEPTED);
                    return reply.getData();
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                connectionManager.messageController.add(connection.receive());
            }
        }

        @Override
        public void close() throws IOException {

        }
    }
}

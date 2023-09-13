package ab.temp.net;

import ab.model.chat.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Server is about to run");

        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Chat server is running.");
            System.out.println("*******************");
            System.out.println(
                    InetAddress.getLocalHost().getHostAddress()

            );
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            socket.close();

//            192.168.56.1
//            while (true) {
//                Socket newConnection = serverSocket.accept();
//                new Handler(newConnection).start();
//            }
        } catch (IOException e) {
            System.out.println("Server error.");
        }
    }

    public static void sendBroadcastMessage(Message message) {
//        connectionMap.values().forEach(connection -> {
//            try {
//                connection.send(message);
//            } catch (IOException e) {
//                System.out.println("Server failed to send a message to " + connection.getRemoteSocketAddress());
//            }
//        });
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String userName = null;
            System.out.println("New connection: " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection()) {
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message());
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Exchange error with: " + socket.getRemoteSocketAddress());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message());
                    System.out.println("Connection closed. " + socket.getRemoteSocketAddress());
                }
            }
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
//                connection.send(new Message(NAME_REQUEST));
//                Message reply = connection.receive();
//                if (reply.getType() != USER_NAME ||
//                        reply.getData().isEmpty() ||
//                        connectionMap.containsKey(reply.getData()))
//                    continue;
//                connectionMap.put(reply.getData(), connection);
//                connection.send(new Message(NAME_ACCEPTED));
//                return reply.getData();
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
//            for (String name : connectionMap.keySet()) {
//                if (!userName.equals(name)) connection.send(new Message(USER_ADDED, name));
//            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
//            while (true) {
//                Message message = connection.receive();
//                if (message.getType() == TEXT) {
//                    sendBroadcastMessage(new Message(TEXT, userName + ": " + message.getData()));
//                } else {
//                    System.out.println("Message type error. " + connection.getRemoteSocketAddress());
//                }
//            }
        }
    }
}
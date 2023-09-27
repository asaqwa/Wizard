package ab.network;

import ab.model.chat.Message;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.Socket;

import static ab.network.Util.*;
import static ab.model.chat.MessageType.*;

public class Client extends NetworkUnit {
    private InterfaceAddress ia;
    private byte[] serverSocket;
    private String password;
    private Connection connection;

    public Client(NetworkController networkController, byte[] serverSocket, InterfaceAddress ia, String password) {
        super(networkController);
        this.ia = ia;
        this.serverSocket = serverSocket;
        this.password = password;
    }

    @Override
    void launch() {
        Thread thread = new ClientHandler();
        thread.start();
    }

    @Override
    public void close() throws IOException {

    }

    class ClientHandler extends Thread {
        @Override
        public void run() {
            try (Socket socket = new Socket(getIP(serverSocket), getPort(serverSocket), ia.getAddress(), 0);
                 Connection connection = new Connection(socket)) {
                Client.this.connection = connection;
                if(clientIfPasswordCorrect()) {
                    clientHandshake();
                    networkController.setClientUnit(Client.this);
                    clientMainLoop();
                } else wrongPassword();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean clientIfPasswordCorrect() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType()== PASSWORD_REQUEST) {
                    connection.send(new Message(PASSWORD_REPLY, password));
                    message = connection.receive();
                    if (PASSWORD_ACCEPTED == message.getType()) {
                        return true;
                    }
                    if (CONNECTION_REJECTED == message.getType()) {
                        return false;
                    }
                    password = networkController.getNewPassword();
                }
            }
        }

        private void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                userName = networkController.getName(userName);
                connection.send(new Message(USER_NAME, userName));
                Message message = connection.receive();
                if (message.getType()==NAME_ACCEPTED) return;
            }
        }

        private void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                networkController.messageController.add(connection.receive());
            }
        }

        private void wrongPassword() {
            networkController.wrongPassword();
        }
    }
}

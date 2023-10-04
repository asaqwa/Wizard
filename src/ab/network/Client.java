package ab.network;

import ab.control.Controller;
import ab.model.chat.Message;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.Socket;

import static ab.network.Util.*;
import static ab.model.chat.MessageType.*;

public class Client extends PrimaryNetworkUnit {
    private final InterfaceAddress ia;
    private final byte[] serverSocket;
    private String password;
    private Connection connection;

    public Client(Controller controller, NetworkController networkController, byte[] serverSocket, InterfaceAddress ia, String password) {
        super(controller, networkController);
        this.ia = ia;
        this.serverSocket = serverSocket;
        this.password = password;
    }

    @Override
    void send(Message message) {
        try {
            connection.send(message);
        } catch (IOException e) {
            controller.messageDeliver(Message.CONNECTION_CLOSED);
            close();
        }
    }

    @Override
    void launch() {
        startTask(new ClientHandler());
    }

    class ClientHandler implements Runnable {
        @Override
        public void run() {
            try (Socket socket = new Socket(getIP(serverSocket), getPort(serverSocket), ia.getAddress(), 0);
                 Connection connection = new Connection(socket)) {
                registerResource(connection);
                Client.this.connection = connection;
                if(clientIfPasswordCorrect()) {
                    clientHandshake();
                    controller.showGame(Client.this);
                    clientMainLoop();
                } else passwordCheckFailed();
            } catch (IOException | ClassNotFoundException ignore) {}
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
                    password = controller.passwordRequest();
                }
            }
        }

        private void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                userName = controller.userNameRequest(userName);
                connection.send(new Message(USER_NAME, userName));
                Message message = connection.receive();
                if (message.getType()==NAME_ACCEPTED) return;
            }
        }

        private void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                controller.messageDeliver(message);
                if (CONNECTION_CLOSED == message.getType())return;
            }
        }

        private void passwordCheckFailed() {
            controller.showPasswordRejected();
        }
    }
}

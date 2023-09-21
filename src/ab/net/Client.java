package ab.net;

import ab.model.chat.Message;
import ab.net.exceptions.StartNewConnectionException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static ab.model.chat.MessageType.*;

public class Client extends NetworkUnit {
    private Socket socket;

    public Client(InetAddress serverAddress, int port) throws StartNewConnectionException {
        try {
            socket = new Socket(serverAddress, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new StartNewConnectionException();
        }
    }

    public void send(Message message) throws IOException {
        out.writeObject(message);
    }

    private Message receive() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    @Override
    public void close() throws Exception {
        try {
            if (socket!=null) socket.close();
        } catch (Exception ignore) {}
        try {
            if (in!=null) in.close();
        } catch (Exception ignore) {}
        try {
            if (out!=null) out.close();
        } catch (Exception ignore) {}
    }

    private class ClientHandler extends Thread {
        @Override
        public void run() {
            try {
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {

            }
        }

        private void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message serverReply = receive();
                if (serverReply.getType() == NAME_REQUEST) {
                    send(new Message("Player_", USER_NAME));
                } else if (serverReply.getType() == NAME_ACCEPTED) {
//                    notifyConnectionStatusChanged(true);
                    return;
                } else
                    throw new IOException("Unexpected MessageType");
            }
        }

        private void clientMainLoop() {
//            while (true) {
//                Message message = receive();
//                if (message.getType() == TEXT) {
//                    processIncomingMessage(message.getData());
//                } else if (message.getType() == USER_ADDED) {
//                    informAboutAddingNewUser(message.getData());
//                } else if (message.getType() == USER_REMOVED) {
//                    informAboutDeletingNewUser(message.getData());
//                } else
//                    throw new IOException("Unexpected MessageType");
//            }
        }
    }
}

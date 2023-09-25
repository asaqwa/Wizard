package ab.network;

import ab.model.chat.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(java.net.Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    void send(Message message) throws IOException {
        out.writeObject(message);
    }

    Message receive() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    @Override
    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }
}

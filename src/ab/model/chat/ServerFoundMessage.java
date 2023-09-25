package ab.model.chat;

import java.net.InterfaceAddress;
import java.net.SocketAddress;


public class ServerFoundMessage extends Message {
    private final SocketAddress serverSocket;
    private final InterfaceAddress ia;

    public ServerFoundMessage(MessageType type, String text, SocketAddress serverSocket, InterfaceAddress ia) {
        super(type, text);
        this.serverSocket = serverSocket;
        this.ia = ia;
    }
}

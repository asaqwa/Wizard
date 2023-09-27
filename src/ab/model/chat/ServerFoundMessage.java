package ab.model.chat;

import java.net.InterfaceAddress;
import java.net.SocketAddress;


public class ServerFoundMessage extends Message {
    private final byte[] serverSocket;
    private final InterfaceAddress ia;

    public ServerFoundMessage(MessageType type,  byte[] serverSocket, InterfaceAddress ia) {
        super(type, null);
        this.serverSocket = serverSocket;
        this.ia = ia;
    }
}

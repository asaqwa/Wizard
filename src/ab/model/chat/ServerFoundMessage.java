package ab.model.chat;

import java.net.InterfaceAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Objects;


public class ServerFoundMessage extends Message {
    private final byte[] serverData;
    private final InterfaceAddress ia;
    private final String serverName;

    public ServerFoundMessage(MessageType type,  byte[] serverData, InterfaceAddress ia) {
        super(type, null);
        this.serverData = serverData;
        this.ia = ia;
        serverName = new String(serverData, 6, serverData.length-6);
    }

    public String getServerName() {
        return serverName;
    }

    public byte[] getServerData() {
        return serverData;
    }

    public InterfaceAddress getIa() {
        return ia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerFoundMessage that = (ServerFoundMessage) o;
        return Arrays.equals(serverData, that.serverData) && ia.equals(that.ia);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ia);
        result = 31 * result + Arrays.hashCode(serverData);
        return result;
    }
}

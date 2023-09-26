package ab.network;

import ab.control.MessageController;
import ab.network.exceptions.ConnectionError;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {
    final Map<IPWrapper, InterfaceAddress> localNetworks;
    final MessageController messageController;

    private NetworkUnit unit;

    public ConnectionManager(MessageController messageController) throws ConnectionError {
        try {
            this.messageController = messageController;
            localNetworks = getLocalNetworks();

        } catch (ConnectionError e) {
            throw e;
        }
    }

    public void setServerUnit(String serverName, String password) throws ConnectionError {
        closeCurrentUnit();
        unit = new Server(this, serverName, password);
    }

    public void setClientUnit(InterfaceAddress ia, SocketAddress serverSocket) {
        closeCurrentUnit();
        unit = new Client(this);
    }

    public void setServerFinder() {
        closeCurrentUnit();
        unit = new ClientServerFinder(this);
    }

    public void closeCurrentUnit() {
        if (unit!=null) {
            try {
                unit.close();
            } catch (IOException ignore) {}
            unit = null;
            System.gc();
        }

    }

    private Map<IPWrapper, InterfaceAddress> getLocalNetworks() throws ConnectionError {
        HashMap<IPWrapper, InterfaceAddress> localNetworks = new HashMap<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    String hostAddress = ia.getAddress().getHostAddress();
                    if (hostAddress.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                        localNetworks.put(new IPWrapper(ia.getBroadcast().getAddress()), ia);
                    }
                }
            }
            if (localNetworks.isEmpty()) throw new ConnectionError();
            return Collections.unmodifiableMap(localNetworks);
        } catch (SocketException e) {
            throw new ConnectionError();
        }
    }
}

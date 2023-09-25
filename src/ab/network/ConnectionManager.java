package ab.network;

import ab.control.MessageController;
import ab.network.exceptions.ConnectionError;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    final Map<IPWrapper, InterfaceAddress> localNetworks;
    final Map<String, Connection> connections = new ConcurrentHashMap<>();
    final MessageController messageController;

    private final NetworkUnit unit;

    public ConnectionManager(MessageController messageController, String serverName) throws ConnectionError {
        try {
            this.messageController = messageController;
            localNetworks = getLocalNetworks();
            unit = new Server(this, serverName);
        } catch (ConnectionError e) {
            throw e;
        }
    }

    public ConnectionManager(MessageController messageController) throws ConnectionError {
        try {
            this.messageController = messageController;
            localNetworks = getLocalNetworks();
            unit = new Client(this);
        } catch (ConnectionError e) {
            throw e;
        }
    }


    public void addConnection(String userName, Connection connection) {
        connections.put(userName, connection);
    }

    public void removeConnection(String userName) {
        connections.remove(userName);
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

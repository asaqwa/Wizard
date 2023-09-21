package ab.network;


import ab.net.InterfaceData;
import ab.network.exceptions.ConnectionError;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class Connection {
    private final HashMap<String, InetAddress> localNetworks;
    private final boolean isServer;
    private final NetworkUnit unit;
    public Connection(boolean isServer) throws ConnectionError {
        this.isServer = isServer;
        localNetworks = getLocalNetworks();
        if (isServer) unit = new Server(localNetworks);
        else unit = new Client();
    }


    private HashMap<String, InetAddress> getLocalNetworks() throws ConnectionError {
        HashMap<String, InetAddress> localNetworks = new HashMap<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    String hostAddress = ia.getAddress().getHostAddress();
                    if (hostAddress.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                        localNetworks.put(hostAddress, ia.getAddress());
                    }
                }
            }
            if (localNetworks.isEmpty()) throw new ConnectionError();
            return localNetworks;
        } catch (SocketException e) {
            throw new ConnectionError();
        }
    }
}

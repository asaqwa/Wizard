package ab.network;

import ab.network.exceptions.ConnectionError;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Connection {
    final Map<IPWrapper, InterfaceAddress> localNetworks;

    private final boolean isServer;
    private final NetworkUnit unit;

    public Connection(boolean isServer) throws ConnectionError {
        try {
            this.isServer = isServer;
            localNetworks = getLocalNetworks();
            unit = isServer? new Server(this): new Client(this);
        } catch (ConnectionError e) {
            throw e;
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

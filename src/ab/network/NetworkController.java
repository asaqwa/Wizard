package ab.network;

import ab.control.Controller;
import ab.control.MessageController;
import ab.network.exceptions.ConnectionError;

import java.io.IOException;
import java.net.*;

import java.util.*;

public class NetworkController {
    final ArrayList<InterfaceAddress> localNetworks;
    final MessageController messageController;
    private final Controller controller;

    private NetworkUnit unit;

    public NetworkController(MessageController messageController, Controller controller) throws ConnectionError {
        try {
            localNetworks = getLocalNetworks();
        } catch (ConnectionError e) {
            throw e;
        }
        this.controller = controller;
        this.messageController = messageController;
    }

    public void setServerUnit(String serverName, String password) throws ConnectionError {
        closeCurrentUnit();
        unit = new Server(this, serverName, password);
        unit.launch();
    }

    public void initNewClient(byte[] serverSocket, InterfaceAddress ia, String password) {
        Client client = new Client(this, serverSocket, ia, password);
        client.launch();
    }

    void setClientUnit(Client client) {
        closeCurrentUnit();
        unit = client;
    }

    public void setServerFinder() throws UnknownHostException {
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

    String getName(String oldName) {
        return controller.getName(oldName);
    }

    String getNewPassword() {
        return controller.getNewPassword();
    }

    public void wrongPassword() {
        controller.wrongPassword();
    }

    private ArrayList<InterfaceAddress> getLocalNetworks() throws ConnectionError {
        ArrayList<InterfaceAddress> localNetworks = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    String hostAddress = ia.getAddress().getHostAddress();
                    if (hostAddress.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                        localNetworks.add(ia);
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

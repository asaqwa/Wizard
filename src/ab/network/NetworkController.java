package ab.network;

import ab.control.Controller;
import ab.control.MessageController;
import ab.network.exceptions.ConnectionError;

import java.net.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkController {
    final ArrayList<InterfaceAddress> localNetworks;
    final MessageController messageController;
    private final Controller controller;
    private ThreadPoolExecutor threadPool;
    private NetworkUnit unit;
    private boolean log = true;

    public NetworkController(Controller controller, MessageController messageController, boolean log) throws ConnectionError {
        try {
            localNetworks = getLocalNetworks();
        } catch (ConnectionError e) {
            throw e;
        }
        this.controller = controller;
        this.messageController = messageController;
        this.log = log;
    }

    public void setServerUnit(String serverName, String password, String userName) throws ConnectionError {
        closeCurrentUnit();
        setUnit(new Server(controller, this, serverName, password, userName, log));
        unit.launch();
    }

    public void initClient(byte[] serverSocket, InterfaceAddress ia, String password) {
        @SuppressWarnings(value = "resource")
        Client client = new Client(controller, this, serverSocket, ia, password);
        client.launch();
    }

    public void setClientUnit(Client client) throws ConnectionError {
        if (! (unit instanceof ClientServerFinder)) throw new ConnectionError();
        closeCurrentUnit();
        unit = client;
    }

    public void setServerFinderUnit() throws UnknownHostException, ConnectionError {
        closeCurrentUnit();
        unit = new ClientServerFinder(controller, this, log);
        unit.launch();
    }

    private synchronized void setUnit(NetworkUnit unit) {
        this.unit = unit;
    }

    public void closeCurrentUnit() {
        if (unit != null) {
            unit.close();
            unit = null;
            System.gc();
        }

    }

    public ThreadPoolExecutor getThreadPool() {
        if (threadPool==null) {
            threadPool = new ThreadPoolExecutor(0, 50,30L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(50), Util::getDaemonThread);
        }
        return threadPool;
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

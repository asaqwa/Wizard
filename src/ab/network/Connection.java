package ab.network;


import ab.network.exceptions.StartNewConnectionException;


import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;
import java.util.HashMap;

public class Connection {
    static public NetworkUnit unit;
    static HashMap<String, InterfaceData> serverIPs = new HashMap<>();
    static boolean isStarted = false;
    static public Connection instance = null;

    public final long startTime = System.currentTimeMillis();
    final byte controlNumber = (byte) (Math.random()*256) ;


    public static void main(String[] args) {
        proceed();
    }

    public static void proceed() {
        findIPs();

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                instance = new Connection();
                BrdSenderOperator sender = new BrdSenderOperator(instance);
                BrdReceiver brdReceiver = new BrdReceiver(instance, sender);
                brdReceiver.start();
                sender.connectionRequest();
                synchronized (instance) {
                    try {
                        instance.wait(1000);
                    } catch (InterruptedException ignore) {
                    }
                }

            } catch (StartNewConnectionException ignore) {}
        }
    }

    public static void findIPs() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    String hostAddress = ia.getAddress().getHostAddress();
                    if (hostAddress.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                        serverIPs.put(hostAddress, new InterfaceData(ni, ia, hostAddress));
                    }
                }
                ni.getInterfaceAddresses().stream().map(a->a.getAddress().getHostAddress())
                        .filter(s->s.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")).forEach(s->serverIPs.put(s, null));
            }
            if (serverIPs.isEmpty()) throw new RuntimeException("No Connection");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection() {
    }
}
package ab.net;


import ab.control.FatalError;
import ab.net.exceptions.StartNewConnectionException;


import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;
import java.util.HashMap;

public class Connection {
    static public NetworkUnit unit;
    static HashMap<String, InterfaceData> serverIPs = new HashMap<>();
    static public Connection instance = null;

    public final long startTime = System.currentTimeMillis();
    final byte controlNumber = (byte) (Math.random()*256) ;
    boolean isStarted = false;


    public static void main(String[] args) {
        proceed();
    }

    public static void proceed() throws FatalError {
        findIPs();
        instance = new Connection();
        instance.initConnection();
    }

    private void initConnection() throws FatalError {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                BrdSenderOperator sender = new BrdSenderOperator(this);
                BrdReceiver brdReceiver = new BrdReceiver(this, sender);
                brdReceiver.start();
                sender.connectionRequest();
                synchronized (this) {
                    try {
                        wait(1000);
                    } catch (InterruptedException ignore) {}
                }
                if (isStarted) return;
            } catch (StartNewConnectionException ignore) {}
        }
        throw new FatalError();
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
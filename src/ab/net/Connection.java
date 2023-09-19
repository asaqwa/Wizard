package ab.net;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Connection {
    static public NetworkUnit unit;
    static HashMap<String, Integer> serverIPs = new HashMap<>();
    static boolean isStarted = false;

    final long startTime = System.currentTimeMillis();
    final byte controlNumber = (byte) (Math.random()*256) ;


    public static void main(String[] args) {
        proceed();
    }

    public static void proceed() {
        findIP();
        while (!isStarted) {
            Connection connection = new Connection();
            BrdReceiver brdReceiver = new BrdReceiver(connection);
            brdReceiver.start();
            connection.connectionRequest();

//            try {
//                connection.wait();
//            } catch (InterruptedException ignore) {}
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectionRequest() {
        byte[] data = getRequestData();
        for (Map.Entry<String, Integer> entry : serverIPs.entrySet()) {
            int brdPort = 32278;
            InetSocketAddress sa = new InetSocketAddress(entry.getKey(), brdPort);
            System.out.println(sa);
            while (brdPort<32283) {
                try (DatagramSocket socket = new DatagramSocket(sa)) {
                    socket.setBroadcast(true);
                    for (int port = 31278; port < 31283; port++) {
                        DatagramPacket packet
                                = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), port);
                        socket.send(packet);
                    }
                    entry.setValue(brdPort);
                    break;
                } catch (IOException e) {
                    brdPort++;
                }
            }
        }
    }

    private byte[] getRequestData() {
        return new byte[] {BrdReceiver.IP_REQUEST, (byte)(startTime>>>56&255), (byte)(startTime>>>48&255),
                (byte)(startTime>>>40&255), (byte)(startTime>>>32&255), (byte)(startTime>>>24&255), (byte)(startTime>>>16&255),
                (byte)(startTime>>>8&255), (byte)(startTime&255), controlNumber};
    }


    public static void findIP() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                ni.getInterfaceAddresses().stream().map(a->a.getAddress().getHostAddress())
                        .filter(s->s.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")).forEach(s->serverIPs.put(s, null));
            }
            if (serverIPs.isEmpty()) throw new RuntimeException("No Connection");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
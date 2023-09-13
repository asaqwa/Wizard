package ab.net;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Enumeration;

public class Connection {
    static ArrayList<String> serverIPs = new ArrayList<>();
    static final long startTime = System.currentTimeMillis();
    static String brdReply;
    static NetworkUnit unit;
//    private static DatagramSocket socket = null;

    public static void main(String[] args) {
        findIP();
        BrdReceiver brdReceiver = new BrdReceiver();
        brdReceiver.start();
        connectionRequest();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void connectionRequest() {
        for (String brdIP : serverIPs) {
            int brdPort = 32278;
            InetSocketAddress sa = new InetSocketAddress(brdIP, brdPort);
            System.out.println(sa);
            try (DatagramSocket socket = new DatagramSocket(sa)) {

                socket.setBroadcast(true);
                byte[] buffer = {2};
                for (int port = 31278; port < 31283; port++) {
                    DatagramPacket packet
                            = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), port);
                    socket.send(packet);
                }
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void findIP() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.getDisplayName().toLowerCase().contains("host-only")) continue;
                ni.getInterfaceAddresses().stream().map(a->a.getAddress().getHostAddress())
                        .filter(s->s.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")).forEach(s->serverIPs.add(s));
            }
            if (serverIPs.isEmpty()) throw new RuntimeException("No Connection");
            System.out.println("IPs - " + serverIPs);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}


/*

        broadcast("Hello", InetAddress.getByName("255.255.255.255"));
    }

    public static void broadcast(  String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, 4445);
        socket.send(packet);
        socket.close();

*/
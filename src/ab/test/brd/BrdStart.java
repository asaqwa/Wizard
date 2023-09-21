package ab.test.brd;

import java.io.IOException;
import java.net.*;

public class BrdStart {
    static SocketAddress address;
    static final Object monitor = new Object();
    static int index = 0;
    public static void main(String[] args) {
        Receiver receiver = new Receiver(monitor);
        receiver.start();
        sendRequest(receiver);
    }

    private static void sendRequest(Receiver receiver) {
        String[] hosts = null;
        try {
             hosts = new String[] {InetAddress.getLocalHost().getHostAddress(), "127.0.0.1", "10.140.101.223"};
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        for (String s: hosts) {
            SocketAddress sa = new InetSocketAddress(s,43218);
            try (DatagramSocket ds = new DatagramSocket(sa)) {
                System.out.println(
                        ds.getLocalSocketAddress() + " - sender local socket address"
                );
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                DatagramPacket packet = new DatagramPacket(new byte[1000], 1000, address);
                Thread.yield();
                ds.send(packet);
                System.out.println("Packet was sent from " + s);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package ab.test.brd;

import java.io.IOException;
import java.net.*;

public class Receiver extends Thread {
    final Object monitor;
    public Receiver(Object monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        InetAddress ethernetAddress;
        try {
            ethernetAddress = InetAddress.getByName("10.140.101.223");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try(DatagramSocket ds = new DatagramSocket(56579, ethernetAddress)) {
            DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
            System.out.println(
                    ds.getLocalSocketAddress() + " - receiver local SocketAddress"
            );
            BrdStart.address = new InetSocketAddress("255.255.255.255", ds.getLocalPort());
//            BrdStart.address = ds.getLocalSocketAddress();
            System.out.println(BrdStart.address + " - address for DatagramPacket");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                synchronized (monitor) {
                    monitor.notify();
                }
                try {
                    ds.setSoTimeout(200);
                    ds.receive(packet);
                    System.out.println("packet is received");
                    break;
                } catch (IOException e) {
                    System.out.println("packet was not received");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

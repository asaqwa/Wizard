package ab.temp.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Probe extends Thread {
    public static void main(String[] args) {
        new Probe().start();
    }

    @Override
    public void run() {
        try (DatagramSocket ds = new DatagramSocket()) {


            ds.setBroadcast(true);
            System.out.println("Broadcast is aloud - " +
                    ds.getBroadcast()
            );
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            ds.receive(packet);
            System.out.printf("address - %s%n"+"port - %s%n"+"data - %s%n"+"socketAddress - %s%n",
                    packet.getAddress(), packet.getPort(), packet.getData(), packet.getSocketAddress());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

package ab.test.brd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver extends Thread {
    @Override
    public void run() {
        try(DatagramSocket ds = new DatagramSocket(56579)) {
            DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
            ds.setSoTimeout(100);
            ds.setSoTimeout(200);
            ds.receive(packet);
            System.out.println("packet is received");
            ds.setSoTimeout(1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

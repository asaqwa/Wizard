package ab.test.brd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BrdStart {
    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        receiver.start();
        sendRequest();
    }

    private static void sendRequest() {
        try(DatagramSocket ds = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);

            ds.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

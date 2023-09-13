package ab.temp.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Connection extends Thread implements AutoCloseable {
    private final String myIP;

    public Connection() throws UnknownHostException {
        myIP = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void run() {

    }

    public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        DatagramSocket socket = null;
        socket = new DatagramSocket();
        socket.setBroadcast(true);


        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, 4456);
        socket.send(packet);
        socket.close();
    }
}

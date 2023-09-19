package ab.net;

import ab.net.exceptions.BrdReceiverException;
import ab.net.exceptions.SetTimeoutException;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class BrdReceiver extends Thread {
    static final int SERVER = 1;
    static final int IP_REQUEST = 2;
    static final int WAIT = 3;
    static final int FATAL_ERROR = 4;
    static final int SERVER_COLLISION = 5;

    private Connection connection;
    private int port = 31278;
    private ArrayList<DatagramPacket> waitList = new ArrayList<>();

    public BrdReceiver(Connection connection) {
        super("Broadcast Receiver");
        setDaemon(true);
        this.connection = connection;
    }

    @Override
    public void run() {

        for ( ;port < 31283; port++) {
            try (DatagramSocket ds = new DatagramSocket(port)) {
                ds.setBroadcast(true);

                if (initConnection(ds)) {
                    Connection.unit = new Server();
                    mainLoop(ds);
                } else {
                    return;
                }


            } catch (SocketException ignore) {
                System.out.println("port is closed: " + port);
            } catch (SetTimeoutException e) {
                System.out.println("set timeout error");
            }
        }
        throw new BrdReceiverException("all ports are closed");
    }

    private boolean initConnection(DatagramSocket ds) {
        final int packetSize = 20;
        DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);

        try {
            ds.setSoTimeout(100);
        } catch (SocketException e) {
            throw new SetTimeoutException();
        }

        try {
            while (true) {
                ds.receive(packet);

                if (Connection.serverIPs.containsKey(packet.getAddress().getHostAddress())) {
                    continue;
                }

                byte[] data = packet.getData();
                if (data[0] == SERVER) {
                    Connection.unit = new Client(packet.getAddress(), packet.getPort());
                    return false;
                } else if (data[0] == IP_REQUEST) {
                    long otherTime = IntStream.range(1, 9).mapToLong(i -> data[i] & 255L).reduce((a, b) -> (a << 8) | b).orElse(0);
                    if (otherTime > connection.startTime) {

                    }
                    System.out.println("IP request received");
                }
            }
        } catch(SocketTimeoutException e){
            return true;
        } catch(IOException e){
            System.out.println("packet error");
        }
        return false;
    }


    private void mainLoop(DatagramSocket ds) {
        try {
            ds.setSoTimeout(0);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendServerInfo() {

    }
}
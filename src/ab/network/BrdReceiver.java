package ab.network;

import ab.network.exceptions.StartNewConnectionException;

import java.io.IOException;

import java.net.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static ab.network.BrdType.*;

public class BrdReceiver extends Thread implements AutoCloseable {

    private int port = 31278;
    private ArrayList<DatagramPacket> waitList = new ArrayList<>();
    private boolean serverIsCaught = false;
    private final Connection connection;
    private DatagramSocket ds;
    private BrdSenderOperator sender;

    BrdReceiver(Connection connection, BrdSenderOperator sender) throws StartNewConnectionException {
        super("Broadcast Receiver");
        setDaemon(true);
        this.connection = connection;
        this.sender = sender;

        for ( ;port < 31283; port++) {
            try {
                ds = new DatagramSocket(port);
                ds.setBroadcast(true);
                return;
            } catch (SocketException ignore) {}
        }
        throw new StartNewConnectionException();
    }

    @Override
    public void run() {
        initRequest(ds);

        if (serverIsCaught) {
            mainLoop(ds);
        }
    }

    private void initRequest(DatagramSocket ds) throws StartNewConnectionException {
        final int packetSize = 20;
        DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);

        try {
            ds.setSoTimeout(100);
        } catch (SocketException e) {
            throw new StartNewConnectionException();
        }

        try {
            while (true) {
                ds.receive(packet);

                if (Connection.serverIPs.containsKey(packet.getAddress().getHostAddress())) {
                    continue;
                }

                byte[] data = packet.getData();
                if (data[0] == SERVER) {
                    startClient(packet.getAddress(), packet.getPort()); // not right
                } else if (data[0] == IP_REQUEST) {
                    long otherTime = IntStream.range(1, 9).mapToLong(i -> data[i] & 255L).reduce((a, b) -> (a << 8) | b).orElse(0);
                    if (otherTime > connection.startTime) {
                        sender.send(new byte[] {WAIT}, packet.getPort(), packet.getAddress().toString());
                    }
                }
            }
        } catch(SocketTimeoutException e){

        } catch(IOException e){
            System.out.println("packet error");
        }
    }

    private void startServer() {
        Connection.unit = new Server(this);
        synchronized (connection) {
            connection.notify();
        }
    }
    private void startClient(InetAddress address, int port) {
        serverIsCaught = true;
        Connection.unit = new Client(address, port);
        synchronized (connection) {
            connection.notify();
        }
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

    @Override
    public void close() {
        if (ds != null) {
            try {
                ds.close();
            } catch (Exception ignore) {}
        }
        sender.close();
    }
}
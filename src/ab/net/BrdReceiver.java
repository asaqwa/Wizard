package ab.net;

import ab.net.exceptions.StartNewConnectionException;

import java.io.IOException;

import java.net.*;

import java.util.HashMap;
import java.util.stream.IntStream;

import static ab.net.BrdType.*;

/*
*   Broadcast packet structure:
*       1       2-9     10
*
*
*
*
*
* */
public class BrdReceiver extends Thread implements AutoCloseable {

    private int port = 31278;
    private HashMap<InetAddress, Integer> waitList = new HashMap<>();
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
            while (!connection.isStarted) {
                ds.receive(packet);

                if (Connection.serverIPs.containsKey(packet.getAddress().getHostAddress())) {
                    continue;
                }

                byte[] data = packet.getData();
                if (data[0] == SERVER) {
                    startClient(packet.getAddress(), packet.getPort()); // not right !!!!!!!!!!!!!!!!!!
                } else if (data[0] == IP_REQUEST) {
                    long otherTime = IntStream.range(1, 9).mapToLong(i -> data[i] & 255L).reduce((a, b) -> (a << 8) | b).orElse(0);
                    if (otherTime > connection.startTime) {
                        waitSequence(packet.getAddress(), packet.getPort());
                    } else if (otherTime < connection.startTime) {
                        waitReport(packet.getAddress(), packet.getPort());
                    } else {
                        executeCollision(packet.getAddress(), packet.getPort(), ds);
                    }
                } else if (data[0] == WAIT) {
                    waitSequence(packet.getAddress(), packet.getPort());
                }
            }
        } catch(SocketTimeoutException e){
            startServer();
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

    private void waitReport(InetAddress address, int port) {

    }

    private void waitSequence(InetAddress address, int port) {
        waitList.put(address, port);
    }

    private void executeCollision(InetAddress address, int port, DatagramSocket ds) {
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
package ab.network;

import ab.network.exceptions.BrdSenderException;
import ab.network.exceptions.StartNewConnectionException;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import static ab.network.BrdType.*;

public class BrdSenderOperator implements AutoCloseable {
    HashMap<String, Sender> senders = new HashMap<>();
    Connection connection;
    InetAddress brdAddress;

    BrdSenderOperator(Connection connection) {
        this.connection = connection;
        try {
            brdAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            throw new StartNewConnectionException();
        }
        initSockets();
    }

    private void initSockets() throws StartNewConnectionException {
        for (InterfaceData iaData : Connection.serverIPs.values()) {
            for (int prt = 32278; prt<32283; prt++) {
                try {
                    senders.put(iaData.hostAddress, new Sender(new DatagramSocket( prt, iaData.interfaceAddress.getAddress())));
                    break;
                } catch (SocketException ignore) {}
            }
        }
        if (senders.isEmpty()) throw new StartNewConnectionException();
    }

    void send(byte[] data) {
        for (Sender sender: senders.values()) {
            for (int port = 31278; port < 31283; port++) {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, brdAddress, port);
                    sender.ds.send(packet);
                } catch (IOException ignore) {}
            }
        }
    }

    void send(byte[] data, int port, String hostAddress) throws BrdSenderException {
        try {
            senders.get(hostAddress).ds.send(getPacket(data, port));
        } catch (IOException e) {
            try {
                senders.remove(hostAddress).close();
            } catch (Exception ignore) {}
            throw new BrdSenderException();
        }

    }

    private DatagramPacket getPacket(byte[] data, int port) {
        return new DatagramPacket(data, data.length, brdAddress, port);
    }

    void connectionRequest() throws BrdSenderException {
        byte[] data = getRequestData();

        for (Sender sender: senders.values()) {
            for (int port = 31278; port < 31283; port++) {
                DatagramPacket packet = getPacket(data, port);
                try {
                    sender.ds.send(packet);
                } catch (IOException e) {
                    throw new BrdSenderException();
                }
            }
        }
    }


    private byte[] getRequestData() {
        long startTime = connection.startTime;
        return new byte[] {IP_REQUEST, (byte)(startTime>>>56&255), (byte)(startTime>>>48&255),
                (byte)(startTime>>>40&255), (byte)(startTime>>>32&255), (byte)(startTime>>>24&255), (byte)(startTime>>>16&255),
                (byte)(startTime>>>8&255), (byte)(startTime&255), connection.controlNumber};
    }

    @Override
    public void close() {
        for (Sender sender: senders.values()) {
            if (sender != null) {
                try {
                    sender.close();
                } catch (Exception ignore) {}
            }
        }
    }


    private static class Sender implements AutoCloseable {
        DatagramSocket ds;

        Sender(DatagramSocket ds) {
            this.ds = ds;
        }

        @Override
        public void close() {
            if (ds != null) {
                try {
                    ds.close();
                } catch (Exception ignore) {}
            }
        }
    }
}

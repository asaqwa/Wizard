package ab.network;

import ab.network.exceptions.ConnectionError;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends NetworkUnit {
    HashMap<IPWrapper, Handler> handlers;
    ArrayList<Socket> establishedConnections = new ArrayList<>();


    public Server(Connection connection) throws ConnectionError {
        super(connection);
        initHandlers();
        handlers.values().forEach(Handler::start);
    }

    private void initHandlers() throws ConnectionError {
        for (Map.Entry<IPWrapper, InterfaceAddress> localNetwork : connection.localNetworks.entrySet()) {
            try {
                handlers.put(localNetwork.getKey(), new ServerHandler(localNetwork.getValue()));
            } catch (IOException ignore) {}
        }
        if (handlers.isEmpty()) throw new ConnectionError();
    }

    @Override
    public void close() {
        for (Handler handler: handlers.values()) {
            try {
                handler.close();
            } catch (IOException ignore) {}
        }
    }

    class ServerHandler extends Handler {
        private final ServerSocket socket;
        private final ServerBrdListener brdListener;

        ServerHandler(InterfaceAddress ia) throws IOException {
            socket = new ServerSocket(0, 20, ia.getAddress());
            brdListener = new ServerBrdListener(ia, getReply());
            brdListener.start();
        }

        byte[] getReply() {
            byte[] ip = socket.getInetAddress().getAddress();
            return new byte[] {ip[0],ip[1],ip[2],ip[3],(byte)(socket.getLocalPort()>>>8),(byte)socket.getLocalPort()};
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    establishedConnections.add(socket.accept());
                } catch (IOException ignore) {}
            }
        }

        @Override
        public void close() throws IOException {
            if (brdListener != null) brdListener.close();
            if (socket != null) socket.close();
        }
    }

    class ServerBrdListener extends BrdListener {
        private final DatagramSocket receiver;
        private final DatagramSocket sender;
        private final byte[] reply;

        ServerBrdListener(InterfaceAddress ia, byte[] reply) throws IOException {
            receiver = new DatagramSocket(19819, ia.getAddress());
            sender = getSender(ia);
            this.reply = reply;
        }

        private DatagramSocket getSender(InterfaceAddress ia) throws ConnectionError {
            for (int port = 19820; port <= 65536; port++) {
                try {
                    return new DatagramSocket(port, ia.getAddress());
                } catch (SocketException ignore) {}
            }
            throw new ConnectionError();
        }

        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[0], 0);
            while(!isInterrupted()) {
                try {
                    receiver.receive(packet);
                    sender.send(new DatagramPacket(reply, 6, packet.getSocketAddress()));
                } catch (IOException ignore) {}
            }
            close();
        }

        @Override
        public void close() {
            if (receiver != null) receiver.close();
            if (sender != null) sender.close();
        }
    }
}

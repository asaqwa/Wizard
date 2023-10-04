package ab.network;

import ab.log.Log;
import ab.model.chat.Message;
import ab.model.chat.MessageType;
import ab.model.chat.ServerFoundMessage;
import ab.network.exceptions.ConnectionError;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;

public class ClientServerFinder extends NetworkUnit {
    private final DatagramPacket BRD_REQUEST;
    private final boolean log;

    public ClientServerFinder(NetworkController networkController, boolean log) throws UnknownHostException {
        super(networkController);
        BRD_REQUEST = new DatagramPacket(new byte[0], 0, InetAddress.getByName("255.255.255.255"), 19819);
        this.log = log;
    }

    @Override
    void send(Message ignore) {}

    @Override
    void launch() throws ConnectionError {
        for (InterfaceAddress ia : networkController.localNetworks) {
            startTask(new ClientBrdReceiverCell(ia));
        }
        startTask(new ClientBrdSenderHeap());
    }

    class ClientBrdReceiverCell implements Runnable {
        private final InterfaceAddress ia;

        ClientBrdReceiverCell(InterfaceAddress ia) {
            this.ia = ia;
        }

        @Override
        public void run() {
            try (DatagramSocket receiver = new DatagramSocket(19819, ia.getAddress())) {
                if (log) Log.log("client broadcast receiver is ready");
                registerResource(receiver);
                while (!Thread.currentThread().isInterrupted()) {
                    DatagramPacket packet = new DatagramPacket(new byte[124], 124);
                    receiver.receive(packet);
                    if (log) Log.log("client brd found a server from: " + packet.getAddress());
                    Log.log(new String(packet.getData()));
                    networkController.messageController.add(new ServerFoundMessage(MessageType.SERVER_FOUND,
                            packet.getData(), ia));
                }
            } catch (IOException ignore) {
            }
        }
    }

    class ClientBrdSenderHeap implements Runnable, Closeable {
        private final DatagramSocket[] senderSockets;

        public ClientBrdSenderHeap() throws ConnectionError {
            senderSockets = networkController.localNetworks.stream()
                        .map(this::createSender).toArray(DatagramSocket[]::new);
            if (senderSockets.length == 0) throw new ConnectionError();
        }

        @Override
        public void run() {
            // senderSocket.close() does not throw an exception
            try (Closeable senderHeap = this) {
                if (log) Log.log("client sender is ready");
                registerResource(senderHeap);
                while (!Thread.currentThread().isInterrupted()) {
                    for (DatagramSocket sender: senderSockets) {
                        sender.send(BRD_REQUEST);
                    }
                    if (log) Log.log("client sender sent request");
                    Thread.sleep(1500);
                }
            } catch (IOException | InterruptedException | NullPointerException ignore) {}
        }

        private DatagramSocket createSender(InterfaceAddress ia) {
            for (int port = 19820; port <= 65536; port++) {
                try {
                    return new DatagramSocket(port, ia.getAddress());
                } catch (SocketException ignore) {}
            }
            return null;
        }

        @Override
        public void close() {
            for (DatagramSocket senderSocket : senderSockets) senderSocket.close();
        }
    }
}

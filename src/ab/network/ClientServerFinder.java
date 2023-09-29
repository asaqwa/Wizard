package ab.network;

import ab.model.chat.Message;
import ab.model.chat.MessageType;
import ab.model.chat.ServerFoundMessage;
import ab.network.exceptions.ConnectionError;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;

public class ClientServerFinder extends NetworkUnit {
    private final DatagramPacket BRD_REQUEST;

    public ClientServerFinder(NetworkController networkController) throws UnknownHostException {
        super(networkController);
        BRD_REQUEST = new DatagramPacket(new byte[0], 0, InetAddress.getByName("255.255.255.255"), 19819);
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
                registerResource(receiver);
                while (!Thread.currentThread().isInterrupted()) {
                    DatagramPacket packet = new DatagramPacket(new byte[6], 6);
                    receiver.receive(packet);
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
                registerResource(senderHeap);
                while (!Thread.currentThread().isInterrupted()) {
                    for (DatagramSocket sender: senderSockets) {
                        sender.send(BRD_REQUEST);
                    }
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
            Thread.currentThread().interrupt();
        }
    }
}

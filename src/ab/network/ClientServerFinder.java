package ab.network;

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
        threadPool.setMaximumPoolSize(networkController.localNetworks.size()+1);
    }

    @Override
    void launch() throws ConnectionError {
        for (InterfaceAddress ia : networkController.localNetworks) {
            startTask(new ClientBrdReceiverCell(ia));
            if (getCorePoolSize() == 0) throw new ConnectionError();
            startTask(new ClientBrdSenderCell());
        }
    }

    @Override
    public void close() {
        super.close();
    }

    class ClientBrdReceiverCell implements Runnable {
        private final InterfaceAddress ia;

        ClientBrdReceiverCell(InterfaceAddress ia) {
            this.ia = ia;
        }

        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[0], 0);
            try (DatagramSocket receiver = new DatagramSocket(19819, ia.getAddress())) {
                registerResource(receiver);
                while (!Thread.currentThread().isInterrupted()) {
                    receiver.receive(packet);
                    networkController.messageController.add(new ServerFoundMessage(MessageType.SERVER_FOUND,
                            packet.getData(), ia));
                }
            } catch (IOException ignore) {
            }
        }
    }

    class ClientBrdSenderCell implements Runnable {
        private final DatagramSocket[] senderSockets = networkController.localNetworks.stream()
                .map(this::createSender).toArray(DatagramSocket[]::new);

        @Override
        public void run() {
            try (Closeable cell = ()-> {for (DatagramSocket senderSocket: senderSockets) senderSocket.close();}) {
                registerResource(cell);
                while (!Thread.currentThread().isInterrupted()) {
                    for (DatagramSocket sender: senderSockets) {
                        sender.send(BRD_REQUEST);
                    }
                    Thread.sleep(1500);
                }
            } catch (IOException | InterruptedException ignore) {}
        }

        private DatagramSocket createSender(InterfaceAddress ia) {
            for (int port = 19820; port <= 65536; port++) {
                try {
                    return new DatagramSocket(port, ia.getAddress());
                } catch (SocketException ignore) {}
            }
            return null;
        }
    }
}

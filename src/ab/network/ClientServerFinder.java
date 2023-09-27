package ab.network;

import ab.model.chat.MessageType;
import ab.model.chat.ServerFoundMessage;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientServerFinder extends NetworkUnit {
    private DatagramPacket BRD_REQUEST = null;
    private ThreadPoolExecutor executor;

    public ClientServerFinder(NetworkController networkController) throws UnknownHostException {
        super(networkController);
        BRD_REQUEST = new DatagramPacket(new byte[0], 0, InetAddress.getByName("255.255.255.255"), 19819);
        executor = new ThreadPoolExecutor(0, 50,30L,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(50));

    }

    @Override
    void launch() {
        int i = 0;
        for (InterfaceAddress ia : networkController.localNetworks) {
            try {
                executor.execute(new ClientBrdListener(ia));
                executor.setCorePoolSize(++i);
            } catch (IOException ignore) {}
        executor.execute(new ClientBrdSender());
        }
    }

    private DatagramSocket getSender(InterfaceAddress ia) {
        for (int port = 19820; port <= 65536; port++) {
            try {
                return new DatagramSocket(port, ia.getAddress());
            } catch (SocketException ignore) {}
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        try {
            this.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
    }

    class ClientBrdListener implements Runnable {
        private final InterfaceAddress ia;

        ClientBrdListener(InterfaceAddress ia) throws IOException {
            this.ia = ia;
        }

        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[0], 0);
            try (DatagramSocket receiver = new DatagramSocket(19819, ia.getAddress())) {
                while (true) {
                    receiver.receive(packet);
                    networkController.messageController.add(new ServerFoundMessage(MessageType.SERVER_FOUND,
                            packet.getData(), ia));
                }
            } catch (IOException ignore) {
            }
        }
    }

    class ClientBrdSender implements Runnable {
        private final DatagramSocket[] senderSockets = networkController.localNetworks.stream()
                .map(ClientServerFinder.this::getSender).toArray(DatagramSocket[]::new);

        @Override
        public void run() {
            try (Closeable ignored = ()-> {for (DatagramSocket senderSocket: senderSockets) senderSocket.close();}) {
                while (true) {
                    for (DatagramSocket sender: senderSockets) {
                        sender.send(BRD_REQUEST);
                    }
                    Thread.sleep(1500);
                }
            } catch (IOException | InterruptedException ignored) {}
        }
    }

    class ClientConnectionBuilder extends ConnectionBuilder {

        @Override
        void launch() {

        }

        @Override
        public void run() {

        }

        @Override
        public void close() throws IOException {

        }
    }
}

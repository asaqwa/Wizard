package ab.network;

import ab.model.chat.MessageType;
import ab.model.chat.ServerFoundMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InterfaceAddress;

public class ClientServerFinder extends NetworkUnit {

    public ClientServerFinder(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    private void initConnection() {

    }


    @Override
    public void close() throws IOException {
        try {
            this.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    class ClientBrdListener extends BrdListener {
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
                    connectionManager.messageController.add(new ServerFoundMessage(MessageType.SERVER_FOUND,
                            new String(packet.getData()), packet.getSocketAddress(), ia));
                }
            } catch (IOException ignore) {
            }
        }

        @Override
        public void close() throws IOException {
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

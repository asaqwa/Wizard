package ab.network;

import java.io.IOException;

public class Client extends NetworkUnit {

    public Client(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void close() throws IOException {

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

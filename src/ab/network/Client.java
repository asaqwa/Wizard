package ab.network;

import java.io.IOException;

public class Client extends NetworkUnit {

    public Client(Connection connection) {
        super(connection);
    }

    @Override
    public void close() throws IOException {

    }

    class ClientHandler extends Handler {
        @Override
        public void run() {

        }

        @Override
        public void close() throws IOException {

        }
    }
}

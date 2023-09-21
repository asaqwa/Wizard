package ab.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class Server extends NetworkUnit {
    HashMap<String, InetAddress> localNetworks;

    public Server(HashMap<String, InetAddress> localNetworks) {
    }

    @Override
    public void close() throws IOException {

    }
}

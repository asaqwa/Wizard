package ab.network;

import java.io.Closeable;

public abstract class NetworkUnit implements Closeable {
    final Connection connection;

    public NetworkUnit(Connection connection) {
        this.connection = connection;
    }

    abstract class Handler extends Thread implements Closeable {}

    abstract class BrdListener extends Thread implements Closeable {}
}

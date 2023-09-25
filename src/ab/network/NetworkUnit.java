package ab.network;

import java.io.Closeable;

public abstract class NetworkUnit implements Closeable {
    final ConnectionManager connectionManager;

    public NetworkUnit(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    abstract class ConnectionBuilder extends Thread implements Closeable {

        abstract void launch();
    }

    abstract class BrdListener extends Thread implements Closeable {}

    abstract class Handler extends Thread implements Closeable {}
}

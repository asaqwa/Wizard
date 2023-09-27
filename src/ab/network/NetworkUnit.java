package ab.network;

import java.io.Closeable;

public abstract class NetworkUnit implements Closeable {
    final NetworkController networkController;
    String userName = "";

    public NetworkUnit(NetworkController networkController) {
        this.networkController = networkController;
    }

    abstract void launch();

    abstract class ConnectionBuilder extends Thread implements Closeable {

        abstract void launch();
    }

    abstract class BrdListener extends Thread implements Closeable {}

    abstract class Handler extends Thread implements Closeable {}
}

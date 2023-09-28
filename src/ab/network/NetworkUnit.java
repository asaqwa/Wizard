package ab.network;

import ab.network.exceptions.ConnectionError;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class NetworkUnit implements Closeable {
    final NetworkController networkController;
    final ThreadPoolExecutor threadPool;
    ArrayList<Closeable> registeredResources = new ArrayList<>();

    public NetworkUnit(NetworkController networkController) {
        this.networkController = networkController;
        threadPool = networkController.getThreadPool();
        threadPool.setCorePoolSize(0);
    }

    abstract void launch() throws ConnectionError;

    void startTask(Runnable task) {
        synchronized (threadPool) {
            threadPool.execute(task);
            threadPool.setCorePoolSize(threadPool.getCorePoolSize()+1);
        }
    }

    int getCorePoolSize() {
        return threadPool.getCorePoolSize();
    }

    void registerResource(Closeable resource) {
        synchronized (registeredResources) {
            registeredResources.add(resource);
        }
    }

    @Override
    public void close() {
        synchronized (registeredResources) {
            for (Closeable resource: registeredResources) {
                try {
                    resource.close();
                } catch (IOException ignore) {}
            }
            registeredResources = null;
        }
    }

    abstract class ConnectionBuilder extends Thread implements Closeable {

        abstract void launch();
    }

    abstract class BrdListener extends Thread implements Closeable {}

    abstract class Handler extends Thread implements Closeable {}
}

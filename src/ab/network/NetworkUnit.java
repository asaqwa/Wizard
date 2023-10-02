package ab.network;

import ab.log.Log;
import ab.model.chat.Message;
import ab.network.exceptions.ConnectionError;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class NetworkUnit implements Closeable {
    final NetworkController networkController;
    private final ThreadPoolExecutor threadPool;
    private ArrayList<Closeable> registeredResources = new ArrayList<>();

    public NetworkUnit(NetworkController networkController) {
        this.networkController = networkController;
        threadPool = networkController.getThreadPool();
    }

    abstract void send(Message message);

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
        Log.log("Network unit in close");
        int resAmount;
        synchronized (registeredResources) {
            threadPool.getQueue().forEach(threadPool::remove);
            resAmount = registeredResources.size();
            for (Closeable resource: registeredResources) {
                try {
                    resource.close();
                } catch (IOException ignore) {}
            }
            registeredResources = null;
        }
        synchronized (threadPool) {
            threadPool.setCorePoolSize(threadPool.getCorePoolSize()-resAmount);
        }
    }
}

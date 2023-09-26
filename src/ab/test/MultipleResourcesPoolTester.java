package ab.test;

import ab.network.Connection;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultipleResourcesPoolTester {
    static long startTime = System.currentTimeMillis();
    static Object monitor = new Object();

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);

        List<Connection> connections = IntStream.range(0,5).mapToObj(i->new Connection()).collect(Collectors.toList());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 50,30L,
                TimeUnit.MINUTES, queue); //, TestConnection::getDaemonThread);
        executor.execute(new Task(connections));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
        print("shutdown");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        print("is finished");
    }

    static Thread getDaemonThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    }

    static String getTime() {
        long l = System.currentTimeMillis() - startTime;
        return String.format("%4s:%03d | ", l/1000, l%1000);
    }

    public static void print(String s) {
        synchronized (monitor) {
            System.out.println(getTime() + Thread.currentThread().getName() + " " + s);
        }
    }

    static class Task extends Thread {
        List<Connection> connections;

        public Task(List<Connection> connections) {
            this.connections = connections;
        }

        @Override
        public void run() {
            try (ConnectionsArray ca = new ConnectionsArray(connections)) {
                print("is ready");
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    print("interrupted");
                }
                print("ends");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ConnectionsArray implements Closeable {
        List<Connection> connections;

        public ConnectionsArray(List<Connection> connections) {
            this.connections = connections;
        }

        @Override
        public void close() throws IOException {
            for (Connection connection : connections) {
                connection.close();
            }
        }
    }

    static class Connection implements Closeable {

        @Override
        public void close() throws IOException {
            print(this.toString() + "Autoclose");
        }
    }
}

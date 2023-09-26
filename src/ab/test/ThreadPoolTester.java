package ab.test;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

public class ThreadPoolTester {
    static long startTime = System.currentTimeMillis();
    static Object monitor = new Object();

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 50,30L,
                TimeUnit.MINUTES, queue); //, TestConnection::getDaemonThread);
        for (int i = 0; i < 50; i++) {
            executor.execute(new Task("task-" + i));
        }
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.setCorePoolSize(5);
        print("poolSize 5");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
        print("shutdown");
        try {
            Thread.sleep(4000);
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

        public Task(String name) {
            super(name);
            setDaemon(true);
        }

        @Override
        public void run() {
            try (Glose glose = new Glose()) {
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

    static class Glose implements Closeable {

        @Override
        public void close() throws IOException {
            print(this.toString() + "Autoclose");
        }
    }
}

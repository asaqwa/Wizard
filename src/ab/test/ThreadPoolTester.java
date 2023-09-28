package ab.test;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.*;


/*
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Util {


    static Thread getDaemonThread(Runnable r) {
        Thread t = factory.newThread(r);
        t.setDaemon(true);
        return t;
    }
}
*/

public class ThreadPoolTester {
    static long startTime = System.currentTimeMillis();
    static final Object monitor = new Object();
    static ArrayList<Closeable> list = new ArrayList<>();
    private static final ThreadFactory factory = Executors.defaultThreadFactory();

    public static void main(String[] args) {
        ArrayList<Closeable> list = ThreadPoolTester.list;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 50,30L,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(50), ThreadPoolTester::getDaemonThread);
        launch(executor, 1, true);
        launch(executor, 10, false);
        try {
            Thread.sleep(3000);
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
//        executor.shutdownNow();
        shutDown(executor);
        print("shutdown");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        print("pollSize 0");
        executor.setCorePoolSize(0);
        ThreadPoolTester.list = list;
        launchGlose(executor, 3, true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
        print("is finished");
    }

    static void launch(ThreadPoolExecutor executor, int limit, boolean changeThreadNumber) {
        for (int i = 1; i <= limit; i++) {
            executor.execute(new Task("task-" + i, executor));
            if (changeThreadNumber) executor.setCorePoolSize(executor.getCorePoolSize()+1);
        }
    }

    static void launchGlose(ThreadPoolExecutor executor, int limit, boolean changeThreadNumber) {
        for (int i = 1; i <= limit; i++) {
            executor.execute(new GloseTask("GLOSE-" + i, executor));
            if (changeThreadNumber) executor.setCorePoolSize(executor.getCorePoolSize()+1);
        }
    }

    static Thread getDaemonThread(Runnable r) {
        Thread t = factory.newThread(r);
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

    public static void addClosable(Closeable task) {
        synchronized (list) {
            list.add(task);
        }
    }

    public static void shutDown(ThreadPoolExecutor executor) {
        synchronized (list) {
            executor.getQueue().forEach(executor::remove);
            for (Closeable closeable : list) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            list = null;
        }
    }

    static class Task implements Runnable {
        ThreadPoolExecutor executor;
        String name;
        public Task(String name, ThreadPoolExecutor executor) {
            this.name = name;
            this.executor = executor;
        }

        @Override
        public void run() {
            try (ServerSocket socket = new ServerSocket(0, 5, InetAddress.getLoopbackAddress())) {
                addClosable(socket);
                String s = String.format("%s is STARTED, getTaskCount %s, getActiveCount %s, getCompletedTaskCount %s, getCorePoolSize %s, getPoolSize %s", name, executor.getTaskCount(), executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getCorePoolSize(), executor.getPoolSize());
                print(s);
//                socket.setSoTimeout(5000);
                socket.accept();

            } catch (IOException | NullPointerException e) {
                print(name + " exception caught: " + e.getClass().getSimpleName());
            } finally {
                String s = String.format("%s is FINISHED, getTaskCount %s, getActiveCount %s, getCompletedTaskCount %s, getCorePoolSize %s, getPoolSize %s", name, executor.getTaskCount(), executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getCorePoolSize(), executor.getPoolSize());
                print(s);
            }
        }
    }

    static class GloseTask implements Runnable {
        ThreadPoolExecutor executor;
        String name;
        public GloseTask(String name, ThreadPoolExecutor executor) {
            this.name = name;
            this.executor = executor;
        }

        @Override
        public void run() {
            try (Glose gl = new Glose()) {
                addClosable(gl);
                String s = String.format("%s is STARTED, getTaskCount %s, getActiveCount %s, getCompletedTaskCount %s, getCorePoolSize %s, getPoolSize %s", name, executor.getTaskCount(), executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getCorePoolSize(), executor.getPoolSize());
                print(s);
                Thread.sleep(5000);

            } catch (IOException | NullPointerException | InterruptedException e) {
                print(name + " exception caught: " + e.getClass().getSimpleName());
            } finally {
                String s = String.format("%s is FINISHED, getTaskCount %s, getActiveCount %s, getCompletedTaskCount %s, getCorePoolSize %s, getPoolSize %s", name, executor.getTaskCount(), executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getCorePoolSize(), executor.getPoolSize());
                print(s);
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

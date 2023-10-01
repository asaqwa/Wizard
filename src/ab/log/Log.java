package ab.log;

public class Log {
    static long startTime = System.currentTimeMillis();
    static final Object monitor = new Object();


    public static void rec(String s) {
        synchronized (monitor) {
            System.out.println(getTime() + Thread.currentThread().getName() + ": " + s);
        }
    }

    static String getTime() {
        long l = System.currentTimeMillis() - startTime;
        return String.format("%4s:%03d | ", l/1000, l%1000);
    }
}

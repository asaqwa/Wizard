package ab.network;

public class TestConnection {
    static long startTime = System.currentTimeMillis();
    static Object monitor = new Object();

    public static void main(String[] args) {


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

}

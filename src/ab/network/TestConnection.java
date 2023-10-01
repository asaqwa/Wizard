package ab.network;

import ab.control.Controller;
import ab.control.MessageController;
import ab.network.exceptions.ConnectionError;
import javafx.scene.layout.BorderPane;

import java.net.UnknownHostException;

public class TestConnection {
    static long startTime = System.currentTimeMillis();
    static Object monitor = new Object();

    public static void main(String[] args) throws ConnectionError, UnknownHostException, InterruptedException {
//        Controller controller = new Controller(new BorderPane());
//        NetworkController nControl = new NetworkController(new MessageController(), controller);
//        nControl.setServerFinder();
//
//        Thread.sleep(2000);
//
//        nControl.closeCurrentUnit();
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

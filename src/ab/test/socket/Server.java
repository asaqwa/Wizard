package ab.test.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

public class Server extends Thread {
    ServerSocket server;
    @Override
    public void run() {
        try {
            server = new ServerSocket(0, 20, InetAddress.getByName("10.140.101.223"));
        } catch (IOException e) {
            System.out.println("server creating error");
        }

        printServerData();
    }

    private void printServerData() {
        System.out.println("************************");
        System.out.println(server + " - ServerSocket");
        System.out.println(server.getChannel() + " - Server chanel");
        System.out.println(server.getInetAddress() + " - Server InetAddress");
        try {
            System.out.println(server.getReuseAddress() + " - Server getReuseAddress");
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println(server.getLocalSocketAddress() + " - Server getLocalSocketAddress");
        System.out.println(server.getLocalPort() + " - Server getLocalPort");
        System.out.println("***********************************************************************");
    }
}

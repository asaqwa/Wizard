package ab.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class ByteArray {
    public static void main(String[] args) throws Exception {
        InetAddress ia = InetAddress.getLocalHost();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(ia);
            System.out.println( oos.toString());
            System.out.println(Arrays.toString(oos.toString().getBytes()));
            System.out.println(oos.toString().length());
        }
    }
}

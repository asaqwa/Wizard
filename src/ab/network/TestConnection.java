package ab.network;

import ab.network.exceptions.ConnectionError;

import java.util.Arrays;
import java.util.HashMap;

public class TestConnection {
    public static void main(String[] args) throws ConnectionError {
//        Connection server = new Connection(true);

        for (int i = 1; i < 3000; i+=256) {
            byte [] ar = get(i);
            String s = i + " " + Integer.toBinaryString(ar[0]&255) + " " +
                    Integer.toBinaryString(ar[1]&255) + " " + Arrays.toString(get(i));
            System.out.println(
                    s
            );
        }

    }

    static byte[] get (int socket) {
        return  new byte[] {(byte) (socket>>8), (byte) socket};
    }
}

package ab.network;

public class Util {

    static String getIP(byte[] ip) {
        return String.format("%s.%s.%s.%s", ip[0]&255, ip[1]&255, ip[2]&255, ip[3]&255);
    }

    static int getPort(byte[] ip) {
        return ip[4]&255<<8 | (ip[5]&255);
    }
}

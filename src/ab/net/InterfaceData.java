package ab.net;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;

public class InterfaceData {
    final NetworkInterface networkInterface;
    final InterfaceAddress interfaceAddress;
    final String hostAddress;

    public InterfaceData(NetworkInterface networkInterface, InterfaceAddress interfaceAddress, String hostAddress) {
        this.networkInterface = networkInterface;
        this.interfaceAddress = interfaceAddress;
        this.hostAddress = hostAddress;
    }
}

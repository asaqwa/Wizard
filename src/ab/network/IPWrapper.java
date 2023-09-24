package ab.network;

import java.util.Arrays;

public class IPWrapper {
    final byte[] broadcast;

    IPWrapper(byte[] broadcast) throws IllegalArgumentException {
        if (broadcast.length != 4) throw new IllegalArgumentException();
        this.broadcast = broadcast;
    }

    boolean isSame(byte[] broadcast) {
        return Arrays.equals(this.broadcast, broadcast);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(broadcast);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        byte[] that =  ((IPWrapper)o).broadcast;
        return Arrays.equals(broadcast, that);
    }
}

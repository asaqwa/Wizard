package ab.network;

public abstract class BrdType {
    static final byte SERVER = 1;
    static final byte IP_REQUEST = 2;
    static final byte WAIT = 3;
    static final byte FATAL_ERROR = 4;
    static final byte SERVER_COLLISION = 5;
}

package ab.network;

public class Server extends NetworkUnit {
    BrdReceiver receiver;

    public Server(BrdReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void close() {
        receiver.close();
    }
}

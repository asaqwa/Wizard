package ab.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class NetworkUnit implements AutoCloseable {
    ObjectInputStream in;
    ObjectOutputStream out;
}

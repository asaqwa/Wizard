package ab.network;

import ab.network.exceptions.ConnectionError;

public class TestConnection {
    public static void main(String[] args) throws ConnectionError {
        Connection server = new Connection(true);
    }
}

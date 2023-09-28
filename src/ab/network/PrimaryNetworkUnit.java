package ab.network;

public abstract class PrimaryNetworkUnit extends NetworkUnit {
    String userName = "";

    public PrimaryNetworkUnit(NetworkController networkController) {
        super(networkController);
    }
}


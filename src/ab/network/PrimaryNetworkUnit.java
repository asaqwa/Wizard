package ab.network;

import ab.control.Controller;

public abstract class PrimaryNetworkUnit extends NetworkUnit {
    String userName = "";

    public PrimaryNetworkUnit(Controller controller, NetworkController networkController) {
        super(controller, networkController);
    }
}


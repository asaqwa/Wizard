package ab.control;

import ab.model.chat.Message;
import ab.network.Client;
import ab.network.NetworkController;
import ab.network.exceptions.ConnectionError;
import ab.view.*;

import java.net.InterfaceAddress;
import java.net.UnknownHostException;

public class Controller {
    ViewController viewController;
    NetworkController networkController;
    MessageController messageController;
    GameController gameController;
    boolean log = true;


    public Controller(ViewController viewController) {
        this.viewController = viewController;
        messageController = new MessageController();
        messageController.setController(this);
        gameController = new GameController();
        try {
            networkController = new NetworkController(this, messageController, log);
        } catch (ConnectionError e) {}
    }

    public void showHome() {
        networkController.closeCurrentUnit();
        viewController.showHome();
    }

    public void showServer(String serverName, String password, String userName) {
        try {
            networkController.setServerUnit(serverName, password, userName);
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
        viewController.showServer(serverName, userName);
    }

    public void showServerScan() {
        try {
            networkController.setServerFinderUnit();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
        viewController.showServerScan();
    }

    public void initClient(byte[] serverSocket, InterfaceAddress ia, String password) {
        networkController.initClient(serverSocket, ia, password);
    }

    public void showClient(Client client) {
        try {
            networkController.setClientUnit(client);
        } catch (ConnectionError e) {
            throw new RuntimeException(e);
        }
        viewController.showGame();
    }

    public void showGame(Client client) {
        try {
            networkController.setClientUnit(client);
        } catch (ConnectionError e) {
            throw new RuntimeException(e);
        }
        viewController.showGame();
    }

    public void showPasswordRejected() {
        //new window;
    }

    public void showGame() {
        viewController.showGame();
    }

    public void closeNetworkUnit() {
        networkController.closeCurrentUnit();
    }

    public String userNameRequest(String oldName) {
        return viewController.getUserName(oldName);
    }

    public String passwordRequest() {
        return viewController.getPassword();
    }

    public void messageDeliver(Message message) {
        messageController.add(message);
    }




}

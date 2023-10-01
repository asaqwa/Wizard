package ab.control;

import ab.Wizard;
import ab.network.NetworkController;
import ab.network.exceptions.ConnectionError;
import ab.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Controller {
    ViewController viewController;
    NetworkController networkController;
    MessageController messageController;
    GameController gameController;


    public Controller(ViewController viewController) {
        this.viewController = viewController;
        messageController = new MessageController();
        messageController.setController(this);
        gameController = new GameController();
        try {
            networkController = new NetworkController(this, messageController);
        } catch (ConnectionError e) {}
    }

    public String getNewPassword() {
        return "";
    }

    public String getName(String oldName) {
        return "";
    }

    public void wrongPassword() {
        //new window;
    }

}

package ab.view;

import ab.control.Controller;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class Server {
    private Controller controller;
    private Stage primaryStage;

    @FXML
    private void handleCancel() {
        controller.showHome();
    }

    @FXML
    private void handleGame() {
        controller.showGame();
    }

    @FXML
    private void handleStopServer () {
        controller.closeNetworkUnit();
    }

    public void initValues(Controller controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;
    }
}

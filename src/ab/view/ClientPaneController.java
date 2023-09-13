package ab.view;

import ab.control.Controller;
import javafx.fxml.FXML;

public class ClientPaneController {
    Controller controller;

    @FXML
    private void handleCancel() {
        controller.setStart();
    }

    @FXML
    private void handleGame() {
        controller.setGame();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}

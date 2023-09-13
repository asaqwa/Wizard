package ab.view;

import ab.control.Controller;
import javafx.fxml.FXML;

public class GamePaneController {
    Controller controller;

    @FXML
    private void handleStartScene() {
        controller.setStart();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}

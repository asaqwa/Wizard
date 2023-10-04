package ab.view;

import ab.control.Controller;
import javafx.fxml.FXML;

public class Game {
    private Controller controller;

    @FXML
    private void handleStartScene() {
        controller.showHome();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}

package ab.view;

import ab.control.Controller;
import javafx.fxml.FXML;

public class StartPaneController {
    Controller controller;

    @FXML
    private void handleServer() {
        controller.setServer();
    }

    @FXML
    private void handleClient() {
        controller.setClient();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}

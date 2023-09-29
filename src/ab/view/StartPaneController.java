package ab.view;

import ab.control.Controller;
import ab.control.ViewController;
import javafx.fxml.FXML;

public class StartPaneController {
    ViewController viewcontroller;

    @FXML
    private void handleServer() {
        viewcontroller.setServer();
    }

    @FXML
    private void handleClient() {
        viewcontroller.setClient();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

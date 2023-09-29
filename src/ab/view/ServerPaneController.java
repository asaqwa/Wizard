package ab.view;

import ab.control.Controller;
import ab.control.ViewController;
import javafx.fxml.FXML;

public class ServerPaneController {
    ViewController viewcontroller;

    @FXML
    private void handleCancel() {
        viewcontroller.setStart();
    }

    @FXML
    private void handleGame() {
        viewcontroller.setGame();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

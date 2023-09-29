package ab.view;

import ab.control.Controller;
import ab.control.ViewController;
import javafx.fxml.FXML;

public class GamePaneController {
    ViewController viewcontroller;

    @FXML
    private void handleStartScene() {
        viewcontroller.setStart();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

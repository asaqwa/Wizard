package ab.view;

import ab.control.ViewController;
import javafx.fxml.FXML;

public class GameController {
    ViewController viewcontroller;

    @FXML
    private void handleStartScene() {
        viewcontroller.setStart();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

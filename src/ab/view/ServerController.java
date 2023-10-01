package ab.view;

import ab.control.ViewController;
import javafx.fxml.FXML;

public class ServerController {
    ViewController viewcontroller;

    @FXML
    private void handleCancel() {
        viewcontroller.setStart();
    }

    @FXML
    private void handleGame() {
        viewcontroller.setGame();
    }

    @FXML
    private void handleStopServer () {
        viewcontroller.stopServer();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

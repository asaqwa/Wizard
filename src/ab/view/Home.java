package ab.view;

import ab.Wizard;
import ab.control.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {
    private Controller controller;
    private Stage primaryStage;


    @FXML
    private void showStartServerDialog() {
        try {
            ServerDialog dialogController = ServerDialog.start(primaryStage);
            if (dialogController.isOkPressed) {
                controller.showServer(dialogController.serverName.getText(), dialogController.password.getText(), dialogController.userName.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClient() {
        controller.showServerScan();
    }

    public void initValues(Controller controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;
    }
}

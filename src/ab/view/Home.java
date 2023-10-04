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
        GetResourceDialog.passwordDialog(primaryStage);
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/StartServerDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Start new server");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StartServerDialog dialogWindow = loader.getController();
            dialogWindow.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            if (dialogWindow.isOkPressed) {
                controller.showServer(dialogWindow.serverName.getText(), dialogWindow.password.getText());
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

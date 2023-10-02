package ab.view;

import ab.Wizard;
import ab.control.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    ViewController viewcontroller;


    @FXML
    private void showStartServerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/StartServerDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Start new server");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(viewcontroller.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StartServerDialogController dialogWindow = loader.getController();
            dialogWindow.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            if (dialogWindow.isOkPressed) {
                viewcontroller.setServer(dialogWindow.serverName.getText(), dialogWindow.password.getText());
            }

//            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClient() {
        viewcontroller.setClient();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }
}

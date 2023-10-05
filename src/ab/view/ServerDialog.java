package ab.view;

import ab.Wizard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;

public class ServerDialog {
    private Stage dialogStage;
    boolean isOkPressed;


    @FXML
    TextField password;

    @FXML
    TextField serverName;

    @FXML
    TextField userName;


    static ServerDialog start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/ServerDialog.fxml"));

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Start new server");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        dialogStage.setScene(new Scene(loader.load()));

        ServerDialog dialogController = loader.getController();
        dialogController.dialogStage = dialogStage;
        dialogStage.showAndWait();

        return dialogController;
    }

    @FXML
    private void handleServer() {
        if (!serverName.getText().isEmpty()) {
            if (!userName.getText().isEmpty()) {
                isOkPressed = true;
                dialogStage.close();
            } else {
                showAlert("User");
            }
        } else {
            showAlert("Server");
        }
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Invalid " + s.toLowerCase() + " name");
        alert.setHeaderText("Please correct " + s.toLowerCase() + " name");
        alert.setContentText(s + " name must be not empty");
        alert.showAndWait();
    }
}

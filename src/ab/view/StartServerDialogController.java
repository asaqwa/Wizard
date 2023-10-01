package ab.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartServerDialogController {
    private Stage dialogStage;
    boolean isOkPressed;

    @FXML
    TextField password;

    @FXML
    TextField serverName;

    @FXML
    Button ok;
    @FXML
    Button cancel;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleServer() {
        if (!serverName.getText().isEmpty()) {
            isOkPressed = true;
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid server name");
            alert.setHeaderText("Please correct server name");
            alert.setContentText("Server name must be not empty");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleCancel() {
        System.out.println(serverName.getText().length());
        System.out.println(password.getText().length());
        dialogStage.close();
    }
}

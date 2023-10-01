package ab.view;

import ab.Wizard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GetResourceDialogController {
    private Pane root;
    private String resourceName;
    private boolean isOk;
    private Stage dialogWindow;
    @FXML
    private TextField resource;
//    private Label

    public GetResourceDialogController(Pane root, String resourceName) {
        this.root = root;
        this.resourceName = resourceName;
    }

    public static GetResourceDialogController getController(Pane root, String resourceName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/GetResourceDialog.fxml"));
        Stage stage =
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle(resourceName + " request");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(root.getScene().getWindow());
        dialogWindow.setScene(new Scene(loader.load()));
        GetResourceDialogController controller = loader.getController();
        controller.dialogWindow = dialogWindow;
        return controller;
    }

    @FXML
    private void handleOk() {
        isOk = true;
        dialogWindow.close();
    }

    @FXML
    private void handleCancel() {
        dialogWindow.close();
    }

//    {
//        try {
//
//
//
//
//
//            Scene scene = new Scene(page);
//            dialogStage.setScene(scene);
//
//            StartServerDialogController dialogWindow = loader.getController();
//            dialogWindow.setDialogStage(dialogStage);
//
//            dialogStage.showAndWait();
//            if (dialogWindow.isOkPressed) {
//                viewcontroller.setServer(dialogWindow.serverName.getText(), dialogWindow.password.getText());
//            }
//
////            return controller.isOkClicked();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

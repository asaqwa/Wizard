package ab.view;

import ab.Wizard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ResourceRequestDialog {
    private Stage dialogWindow;
    private String warning = "Input must be not empty";;
    private boolean isEmptyAllowed;
    String result;

    @FXML
    private TextField resource;
    @FXML
    private Label resourceRequest;
    @FXML
    private Label resourceName;
    @FXML
    private Label warningMessage;


    public static ResourceRequestDialog getDialogController(Stage primaryStage, String resourceName, boolean isEmptyAllowed, String oldName) {
        FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/ResourceRequestDialog.fxml"));
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle(resourceName);
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(primaryStage);
        try {
            dialogWindow.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ResourceRequestDialog controller = loader.getController();
        controller.init(dialogWindow, resourceName, isEmptyAllowed, oldName);
        return controller;
    }
    private void init(Stage dialogWindow, String resourceName, boolean isEmptyAllowed, String oldName) {
        this.dialogWindow = dialogWindow;
        this.isEmptyAllowed = isEmptyAllowed;
        this.resourceName.setText(resourceName + ":");
        resourceRequest.setText(resourceName + " request");
        if (oldName!= null && !oldName.isEmpty()) {
            warningMessage.setText(oldName + " is already taken");
        }

    }

    public static String getUserName(Stage primaryStage, String oldName) {
        ResourceRequestDialog controller =
                getDialogController(primaryStage, "User name", false, oldName);
        controller.dialogWindow.showAndWait();
        return controller.result;
    }

    public static String getPassword(Stage primaryStage) {
        ResourceRequestDialog controller =
                getDialogController(primaryStage, "Password", true, null);
        controller.dialogWindow.showAndWait();
        return controller.result;
    }

    @FXML
    private void handleOk() {
        if (!isEmptyAllowed && resource.getText().isEmpty()) {
            warningMessage.setText(warning);
        } else {
            result = resource.getText();
            dialogWindow.close();
        }

    }

    @FXML
    private void handleCancel() {
        result = null;
        dialogWindow.close();
    }
}

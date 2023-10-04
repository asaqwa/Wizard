package ab.view;

import ab.Wizard;
import ab.control.Controller;
import ab.network.NetworkController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Predicate;

public class GetResourceDialog {
    private Stage dialogWindow;
    private String name;
    private String warning;
    private boolean isEmptyAllowed;
    String result;

    @FXML
    public TextField resource;
    @FXML
    public Label resourceRequest;
    @FXML
    public Label resourceName;
    @FXML
    public Label warningMessage;


    public static GetResourceDialog getDialogController(Stage primaryStage, String resourceName, boolean isEmptyAllowed, String oldName) {
        FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/GetResourceDialog.fxml"));
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle(resourceName + " request");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(primaryStage);
        try {
            dialogWindow.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GetResourceDialog controller = loader.getController();
        controller.init(dialogWindow, resourceName, isEmptyAllowed,oldName);
        return controller;
    }

    public static String userNameDialog(Stage primaryStage, String oldName) {
        GetResourceDialog controller =
                getDialogController(primaryStage, "User name", false, oldName);
        controller.dialogWindow.showAndWait();
        return controller.result;
    }

    public static String passwordDialog(Stage primaryStage) {
        GetResourceDialog controller =
                getDialogController(primaryStage, "Password", false, null);
        controller.dialogWindow.showAndWait();
        return controller.result;
    }

    private void init(Stage dialogWindow, String resourceName, boolean isEmptyAllowed, String oldName) {
        this.dialogWindow = dialogWindow;
        name = resourceName;
        this.warning = warning;
        this.isEmptyAllowed = isEmptyAllowed;
        this.resourceName.setText(resourceName + ":");
        resourceRequest.setText("Enter " + name.toLowerCase());
        if (oldName!= null) {
            warningMessage.setText(oldName + " is already taken");
        }
        warning = "Input must be not empty";
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

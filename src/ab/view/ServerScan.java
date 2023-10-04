package ab.view;

import ab.control.Controller;
import ab.model.chat.ServerFoundMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ServerScan {
    private Controller controller;
    private final ObservableList<ServerFoundMessage> servers = FXCollections.observableArrayList();
    private Stage primaryStage;

    @FXML
    Button connect;
    @FXML
    Label selectedServerName;
    @FXML
    TableView<ServerFoundMessage> serverTable;
    @FXML
    TableColumn<ServerFoundMessage, String> serverList;
    @FXML
    TextField userName;
    @FXML
    TextField password;



    @FXML
    private void initialize() {
        serverList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServerName()));

        showSelectedServer(null);

        // Слушаем изменения выбора,
        serverTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedServer(newValue.getServerName()));
        serverTable.setItems(servers);
    }

    void showSelectedServer(String serverName) {
        if (serverName == null) {
            selectedServerName.setText("");
            connect.setDisable(true);
        }
        selectedServerName.setText(serverName);
        connect.setDisable(false);
    }

    @FXML
    private void handleCancel() {
        servers.clear();
        controller.showHome();
    }

    @FXML
    private void handleConnect() {
        servers.clear();
        controller.showGame();
    }

    public void initValues(Controller controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;
    }

    public void setNewServer(ServerFoundMessage message) {
        if (!servers.contains(message)) {
            servers.add(message);
        }
    }
}

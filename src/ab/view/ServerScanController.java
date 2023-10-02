package ab.view;

import ab.control.ViewController;
import ab.model.chat.ServerFoundMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ServerScanController {
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

    ViewController viewcontroller;

    private final ObservableList<ServerFoundMessage> servers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        serverList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServerName()));

        // Очистка дополнительной информации об адресате.
//        showSelectedServer(null);

        // deactivation of the buttons
//        deleteButton.setDisable(true);
//        editButton.setDisable(true);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
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
        viewcontroller.setStart();
    }

    @FXML
    private void handleConnect() {
        viewcontroller.setGame();
    }

    public void setController(ViewController viewcontroller){
        this.viewcontroller = viewcontroller;
    }

    public void setNewServer(ServerFoundMessage message) {
        if (!servers.contains(message)) {
            servers.add(message);
        }
    }
}

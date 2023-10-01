package ab.view;

import ab.control.ViewController;
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
    TableView<String> serverTable;
    @FXML
    TableColumn<String, String> serverList;
    @FXML
    TextField userName;
    @FXML
    TextField password;

    ViewController viewcontroller;

    private final ObservableList<String> servers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        serverList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Очистка дополнительной информации об адресате.
//        showSelectedServer(null);

        // deactivation of the buttons
//        deleteButton.setDisable(true);
//        editButton.setDisable(true);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        serverTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedServer(newValue));
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

    public void setNewServer(String serverName) {
        servers.add(serverName);
    }
}

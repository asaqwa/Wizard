package ab.view;

import ab.Wizard;
import ab.control.Controller;
import ab.model.chat.ServerFoundMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewController {
    private final Controller controller;
    private final Stage primaryStage;
    private final BorderPane rootLayout;

    private ServerScan serverScan;
    private Server server;


    Node startPane;
    Node findServer;
    Node serverPane;
    Node gamePane;
    Node gameStat;

    public ViewController(BorderPane rootLayout, Stage stage) {
        this.rootLayout = rootLayout;
        primaryStage = stage;
        controller = new Controller(this);
        initPanes();
        showHome();
    }

    private void initPanes() {
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Home.fxml"));
            startPane = loader.load();
            ((Home) loader.getController()).initValues(controller, primaryStage);


            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/ServerScan.fxml"));
            findServer = loader.load();
            serverScan = (ServerScan) loader.getController();
            serverScan.initValues(controller, primaryStage);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Server.fxml"));
            serverPane = loader.load();
            server = (Server) loader.getController();
            server.initValues(controller, primaryStage);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Game.fxml"));
            gamePane = loader.load();
            ((Game) loader.getController()).setController(controller);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/GameInfo.fxml"));
            gameStat = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showHome() {
        rootLayout.setCenter(startPane);
        rootLayout.setRight(null);
    }

    public void showServer(String serverName, String userName) {
        rootLayout.setCenter(serverPane);
    }

    public void showServerScan() {
        rootLayout.setCenter(findServer);
    }

    public void showGame() {
        rootLayout.setCenter(gamePane);
        rootLayout.setRight(gameStat);
    }

    public String getUserName(String oldName) {
        return ResourceRequestDialog.getUserName(primaryStage, oldName);
    }

    public String getPassword() {
        return ResourceRequestDialog.getPassword(primaryStage);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void newServerRegistration(ServerFoundMessage message) {
        serverScan.setNewServer(message);
    }
}

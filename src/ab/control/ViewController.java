package ab.control;

import ab.Wizard;
import ab.model.chat.ServerFoundMessage;
import ab.network.exceptions.ConnectionError;
import ab.view.*;
import ab.view.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.UnknownHostException;

public class ViewController {
    Controller controller;
    Stage primaryStage;
    BorderPane rootLayout;
    Node startPane;
    Node findServer;
    ServerScanController serverScanController;
    Node serverPane;
    Node gamePane;
    Node gameStat;

    public ViewController(BorderPane rootLayout, Stage stage) {
        this.rootLayout = rootLayout;
        primaryStage = stage;
        initPanes();
        controller = new Controller(this);
        setStart();
    }

    private void initPanes() {
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Home.fxml"));
            startPane = loader.load();
            ((HomeController) loader.getController()).setController(this);


            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/ServerScan.fxml"));
            findServer = loader.load();
            serverScanController = (ServerScanController) loader.getController();
            serverScanController.setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Server.fxml"));
            serverPane = loader.load();
            ((ServerController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/Game.fxml"));
            gamePane = loader.load();
            ((GameController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/fxml/GameInfo.fxml"));
//            ((GameStatisticsController) loader.getController()).setController(this);
            gameStat = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStart() {
        System.out.println();
        controller.networkController.closeCurrentUnit();
        rootLayout.setCenter(startPane);
        rootLayout.setRight(null);
    }

    public void setServer(String serverName, String password) {
        try {
            controller.networkController.setServerUnit(serverName, password);
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
        rootLayout.setCenter(serverPane);
    }

    public void stopServer() {
        controller.networkController.closeCurrentUnit();
    }

    public void setClient() {
        try {
            controller.networkController.setServerFinder();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
        rootLayout.setCenter(findServer);
    }

    public void setGame() {
        rootLayout.setCenter(gamePane);
        rootLayout.setRight(gameStat);
    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

    public void newServer(ServerFoundMessage message) {
        serverScanController.setNewServer(message);
    }
}

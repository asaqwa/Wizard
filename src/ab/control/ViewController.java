package ab.control;

import ab.Wizard;
import ab.network.exceptions.ConnectionError;
import ab.view.*;
import ab.view.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.IOException;
import java.net.UnknownHostException;

public class ViewController {
    Controller controller;
    Window mainWindow;
    BorderPane rootLayout;
    Node startPane;
    Node findServer;
    ServerScanController serverScanController;
    Node serverPane;
    Node gamePane;
    Node gameStat;

    public ViewController(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
//        mainWindow = rootLayout.getScene().getWindow();
        initPanes();
        controller = new Controller(this);
        setStart();
    }

    private void initPanes() {
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/StartPane.fxml"));
            startPane = loader.load();
            ((HomeController) loader.getController()).setController(this);


            loader = new FXMLLoader(Wizard.class.getResource("view/FindServer.fxml"));
            findServer = loader.load();
            serverScanController = (ServerScanController) loader.getController();
            serverScanController.setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/ServerPane.fxml"));
            serverPane = loader.load();
            ((ServerController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/GamePane.fxml"));
            gamePane = loader.load();
            ((GameController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/GameStatistics.fxml"));
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

    public Window getMainWindow() {
        return mainWindow;
    }

    public void newServer(String serverName) {
        serverScanController.setNewServer(serverName);
    }
}

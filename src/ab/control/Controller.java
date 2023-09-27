package ab.control;

import ab.Wizard;
import ab.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Controller {
    BorderPane rootLayout;
    Node startPane;
    Node clientPane;
    Node serverPane;
    Node gamePane;
    Node gameStat;

    public Controller(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
        initPanes();
        setStart();
    }

    private void initPanes() {
        try {
            FXMLLoader loader = new FXMLLoader(Wizard.class.getResource("view/StartPane.fxml"));
            startPane = loader.load();
            ((StartPaneController) loader.getController()).setController(this);


            loader = new FXMLLoader(Wizard.class.getResource("view/ClientPane.fxml"));
            clientPane = loader.load();
            ((ClientPaneController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/ServerPane.fxml"));
            serverPane = loader.load();
            ((ServerPaneController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/GamePane.fxml"));
            gamePane = loader.load();
            ((GamePaneController) loader.getController()).setController(this);

            loader = new FXMLLoader(Wizard.class.getResource("view/GameStatistics.fxml"));
//            ((GameStatisticsController) loader.getController()).setController(this);
            gameStat = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStart() {
        rootLayout.setCenter(startPane);
        rootLayout.setRight(null);
    }

    public void setServer() {
        rootLayout.setCenter(serverPane);
    }

    public void setClient() {
        rootLayout.setCenter(clientPane);
    }

    public void setGame() {
        rootLayout.setCenter(gamePane);
        rootLayout.setRight(gameStat);
    }

    public String getNewPassword() {
        return "";
    }

    public String getName(String oldName) {
        return "";
    }

    public void wrongPassword() {
        //new window;
    }
}

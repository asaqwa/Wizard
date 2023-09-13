package ab;

import ab.control.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Wizard extends Application {
    BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Wizard.class.getResource("view/MainFrame.fxml"));
        rootLayout = (BorderPane) loader.load();

        Controller controller = new Controller(rootLayout);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

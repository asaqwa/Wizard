package ab;

import ab.control.ViewController;
import ab.log.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Wizard extends Application {
    BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Log.log("start");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Wizard.class.getResource("view/fxml/RootWindow.fxml"));
        rootLayout = (BorderPane) loader.load();

        ViewController viewController = new ViewController(rootLayout, primaryStage);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

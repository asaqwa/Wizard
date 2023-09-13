package ab.temp;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Chatter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    ObservableList<ChatEntry> messages;

    @Override
    public void start(Stage primaryStage) {
        messages = FXCollections.observableArrayList();
        TextField textField = new TextField();
        Button localButton = new Button("Local");
        Button remoteButton = new Button("Remote");
        ListView<ChatEntry> listView = new ListView<>();
        listView.setCellFactory(list -> new ChatCell());
        listView.setItems(messages);
        localButton.setOnAction(evt -> messages.add(new ChatEntry(textField.getText(), MessageType.LOCAL)));
        remoteButton.setOnAction(evt -> messages.add(new ChatEntry(textField.getText(), MessageType.REMOTE)));
        Scene scene = new Scene(new VBox(10, listView, new HBox(remoteButton, textField, localButton)));
//        scene.getStylesheets().add("/css/samples.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    enum MessageType {
        LOCAL, REMOTE
    }


    static class ChatCell extends ListCell<ChatEntry> {

        HBox graphic = new HBox();
        Text message = new Text();

        public ChatCell() {
            graphic.getChildren().add(message);
            getStyleClass().add("custom-list-cell");
        }

        @Override
        public void updateItem(ChatEntry item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && (item != null)) {
                message.setText(item.textProperty().get());
                graphic.setAlignment(item.typeProperty().get().equals(MessageType.REMOTE) ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
                setGraphic(graphic);
            } else {
                message.setText("");
                setGraphic(null);
            }
        }
    }


    static class ChatEntry {
        private final StringProperty text = new SimpleStringProperty("");
        private final ObjectProperty<MessageType> type = new SimpleObjectProperty<>();

        public ChatEntry(String text, MessageType type) {
            this.text.set(text);
            this.type.set(type);
        }

        public StringProperty textProperty() {
            return text;
        }

        public ObjectProperty<MessageType> typeProperty() {
            return type;
        }
    }
}
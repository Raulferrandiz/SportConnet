package org.example.sportconnect;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.sportconnect.util.SceneManager;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.initialize(stage);
        SceneManager.show("/org/example/sportconnect/fxml/login.fxml", "SportConnect");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

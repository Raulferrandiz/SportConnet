package org.example.sportconnect.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class SceneManager {
    private static Stage primaryStage;

    private SceneManager() {
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
    }

    public static void show(String fxmlPath, String title) {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneManager not initialized.");
        }

        try {
            URL resource = SceneManager.class.getResource(fxmlPath);
            if (resource == null) {
                throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            }
            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root);
            String css = SceneManager.class.getResource("/org/example/sportconnect/css/app.css").toExternalForm();
            scene.getStylesheets().add(css);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load scene: " + fxmlPath, e);
        }
    }
}

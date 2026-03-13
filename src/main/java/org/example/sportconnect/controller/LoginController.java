package org.example.sportconnect.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sportconnect.service.AuthService;
import org.example.sportconnect.util.SceneManager;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label mensajeLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            authService.login(email, password);

            mensajeLabel.setText("");

            SceneManager.show("/org/example/sportconnect/fxml/home.fxml", "SportConnect - Inicio");

        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(e.getMessage());

        } catch (Exception e) {
            mensajeLabel.setText("Error al iniciar sesión.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void goToRegister() {
        SceneManager.show("/org/example/sportconnect/fxml/register.fxml", "SportConnect - Registro");
    }
}
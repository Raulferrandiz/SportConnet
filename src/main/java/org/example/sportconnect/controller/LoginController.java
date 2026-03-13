package org.example.sportconnect.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sportconnect.model.Usuario;
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

    //Valida el login y dependiendo del rol redirige a una pantalla o a otra
    @FXML
    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            Usuario usuario = authService.login(email, password);

            mensajeLabel.setText("");

            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                SceneManager.show(
                        "/org/example/sportconnect/fxml/admin_deportes.fxml",
                        "SportConnect - Admin"
                );
            } else {
                SceneManager.show(
                        "/org/example/sportconnect/fxml/home.fxml",
                        "SportConnect - Home"
                );
            }

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
package org.example.sportconnect.service;

import org.example.sportconnect.dao.UsuarioDao;
import org.example.sportconnect.model.Usuario;

import java.util.List;

public class AuthService {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    /**
     * Inicio de sesion
     */
    public Usuario login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        //Limpiar espacios
        email = email.trim();
        password = password.trim();

        Usuario usuario = usuarioDao.login(email, password);

        if (usuario == null) {
            throw new IllegalArgumentException("Email o contraseña incorrectos.");
        }

        return usuario;
    }

    /**
     * Registrar un nuevo usuario
     */
    public boolean registrar(String nombre, String email, String password, String repetirPassword) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        if (repetirPassword == null || repetirPassword.isBlank()) {
            throw new IllegalArgumentException("Debes repetir la contraseña.");
        }

        nombre = nombre.trim();
        email = email.trim();
        password = password.trim();
        repetirPassword = repetirPassword.trim();

        if (!password.equals(repetirPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        // Aquí usamos el DAO para buscar por email, por si ya existe el usuario
        if (usuarioDao.existePorEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email.");
        }

        Usuario usuario = new Usuario(nombre, email, password, "USER");
        return usuarioDao.guardar(usuario);
    }

    /**
     * busca un usuario por su id a traves del DAO
     */
    public Usuario buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id del usuario no es válido.");
        }

        return usuarioDao.buscarPorId(id);
    }

    /**
     * Lista de todos los usuarios
     */
    public List<Usuario> listarTodos() {
        return usuarioDao.listarTodos();
    }
}
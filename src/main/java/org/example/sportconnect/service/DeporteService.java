package org.example.sportconnect.service;

import org.example.sportconnect.dao.DeporteDao;
import org.example.sportconnect.model.Deporte;

public class DeporteService {

    private final DeporteDao deporteDao = new DeporteDao();

    /**
     * Crear un deporte validando los campos necesarios
     * @param nombre nombre del deporte
     * @param descripcion descripcion del deporte
     * @param logo ruta o nombre del logo
     * @return true si se guarda correctamente
     */
    public boolean crearDeporte(String nombre, String descripcion, String logo) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del deporte no puede estar vacío.");
        }

        nombre = nombre.trim();

        if (descripcion != null) {
            descripcion = descripcion.trim();
        }

        if (logo != null) {
            logo = logo.trim();
        }

        Deporte deporteExistente = deporteDao.buscarPorNombre(nombre);
        if (deporteExistente != null) {
            throw new IllegalArgumentException("Ya existe un deporte con ese nombre.");
        }

        //Creamos el deporte
        Deporte deporte = new Deporte();
        deporte.setNombre(nombre);
        deporte.setDescripcion(descripcion);
        deporte.setLogo(logo);

        return deporteDao.guardar(deporte);
    }

    /**
     * Editar un deporte validando los datos
     * @param deporteId id del deporte
     * @param nombre nombre nuevo del deporte
     * @param descripcion descripcion nueva
     * @param logo logo nuevo
     * @return true si se actualiza correctamente
     */
    public boolean editarDeporte(Long deporteId, String nombre, String descripcion, String logo) {

        if (deporteId == null || deporteId <= 0) {
            throw new IllegalArgumentException("El id del deporte no es válido.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del deporte no puede estar vacío.");
        }

        nombre = nombre.trim();

        if (descripcion != null) {
            descripcion = descripcion.trim();
        }

        if (logo != null) {
            logo = logo.trim();
        }

        Deporte deporte = deporteDao.buscarPorId(deporteId);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no existe.");
        }

        Deporte deporteMismoNombre = deporteDao.buscarPorNombre(nombre);
        if (deporteMismoNombre != null && !deporteMismoNombre.getId().equals(deporteId)) {
            throw new IllegalArgumentException("Ya existe otro deporte con ese nombre.");
        }

        //Editamos el deporte
        deporte.setNombre(nombre);
        deporte.setDescripcion(descripcion);
        deporte.setLogo(logo);

        return deporteDao.actualizar(deporte);
    }

    //Si existe el deporte que le pasamos se elimina
    public boolean eliminarDeporte(Long deporteId) {
        if (deporteId == null || deporteId <= 0) {
            throw new IllegalArgumentException("El id del deporte no es válido.");
        }

        Deporte deporte = deporteDao.buscarPorId(deporteId);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no existe.");
        }

        return deporteDao.eliminar(deporte);
    }
}
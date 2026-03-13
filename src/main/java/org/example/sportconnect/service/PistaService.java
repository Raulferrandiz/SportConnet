package org.example.sportconnect.service;

import org.example.sportconnect.dao.DeporteDao;
import org.example.sportconnect.dao.PistaDao;
import org.example.sportconnect.dao.ReservaDao;
import org.example.sportconnect.model.Deporte;
import org.example.sportconnect.model.Pista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PistaService {

    private final PistaDao pistaDao = new PistaDao();
    private final DeporteDao deporteDao = new DeporteDao();
    private final ReservaDao reservaDao = new ReservaDao();

    /**
     * Crear una nueva pista validando antes todos los datos
     * @param nombre nombre de la pista
     * @param descripcion descripcion de la pista
     * @param imagen ruta o nombre de la imagen
     * @param deporteId id del deporte al que pertenece
     * @param activa estado de la pista
     * @return true si se guarda correctamente
     */
    public boolean crearPista(String nombre, String descripcion, String imagen, Long deporteId, boolean activa) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la pista no puede estar vacío.");
        }

        if (deporteId == null || deporteId <= 0) {
            throw new IllegalArgumentException("El deporte no es válido.");
        }

        nombre = nombre.trim();

        if (descripcion != null) {
            descripcion = descripcion.trim();
        }

        if (imagen != null) {
            imagen = imagen.trim();
        }

        Deporte deporte = deporteDao.buscarPorId(deporteId);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no existe.");
        }

        //Creamos la pista
        Pista pista = new Pista();
        pista.setNombre(nombre);
        pista.setDescripcion(descripcion);
        pista.setImagen(imagen);
        pista.setDeporte(deporte);
        pista.setActiva(activa);

        return pistaDao.guardar(pista);
    }

    /**
     * Editar una pista validando antes todos los datos
     * @param pistaId id de la pista
     * @param nombre nombre nuevo de la pista
     * @param descripcion descripcion nueva
     * @param imagen imagen nueva
     * @param deporteId id del deporte
     * @param activa estado nuevo de la pista
     * @return true si se actualiza correctamente
     */
    public boolean editarPista(Long pistaId, String nombre, String descripcion, String imagen, Long deporteId, boolean activa) {

        if (pistaId == null || pistaId <= 0) {
            throw new IllegalArgumentException("El id de la pista no es válido.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la pista no puede estar vacío.");
        }

        if (deporteId == null || deporteId <= 0) {
            throw new IllegalArgumentException("El deporte no es válido.");
        }

        nombre = nombre.trim();

        if (descripcion != null) {
            descripcion = descripcion.trim();
        }

        if (imagen != null) {
            imagen = imagen.trim();
        }

        Pista pista = pistaDao.buscarPorId(pistaId);
        if (pista == null) {
            throw new IllegalArgumentException("La pista no existe.");
        }

        Deporte deporte = deporteDao.buscarPorId(deporteId);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no existe.");
        }

        //Editamos datos de la pista
        pista.setNombre(nombre);
        pista.setDescripcion(descripcion);
        pista.setImagen(imagen);
        pista.setDeporte(deporte);
        pista.setActiva(activa);

        return pistaDao.actualizar(pista);
    }

    //Cambiamos el estado de una pista activa/inactiva
    public boolean cambiarEstado(Long pistaId, boolean activa) {
        if (pistaId == null || pistaId <= 0) {
            throw new IllegalArgumentException("El id de la pista no es válido.");
        }

        Pista pista = pistaDao.buscarPorId(pistaId);
        if (pista == null) {
            throw new IllegalArgumentException("La pista no existe.");
        }

        pista.setActiva(activa);
        return pistaDao.actualizar(pista);
    }

    // Lista de pistas disponibles por deporte, fecha y tramo horario
    public List<Pista> listarDisponiblesPorFechaYDeporte(Long deporteId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        if (deporteId == null || deporteId <= 0) {
            throw new IllegalArgumentException("El deporte no es válido.");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }

        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("La hora de inicio y fin son obligatorias.");
        }

        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden consultar pistas para una fecha pasada.");
        }

        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        //Obtener pista por el deporte que queramos
        List<Pista> pistas = pistaDao.listarPorDeporte(deporteId);

        //Después filtramos solo las activas y las que no estén ocupadas en ese tramo horario
        return pistas.stream()
                .filter(Pista::isActiva)
                .filter(pista -> !reservaDao.existeReserva(pista.getId(), fecha, horaInicio, horaFin))
                .toList();
    }
}
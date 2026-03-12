package org.example.sportconnect.service;

import org.example.sportconnect.dao.PistaDao;
import org.example.sportconnect.dao.ReservaDao;
import org.example.sportconnect.dao.UsuarioDao;
import org.example.sportconnect.model.Pista;
import org.example.sportconnect.model.Reserva;
import org.example.sportconnect.model.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class ReservaService {

    // Los DAO se usan desde aquí para no meter Hibernate en los controllers
    private final ReservaDao reservaDao = new ReservaDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final PistaDao pistaDao = new PistaDao();

    /**
     * Crea una nueva reserva validando antes todos los datos
     * @param usuarioId id del usuario que reserva
     * @param pistaId id de la pista
     * @param fecha fecha de la reserva
     * @param horaInicio hora de inicio
     * @param horaFin hora de fin
     * @return true si se guarda correctamente
     */
    public boolean crearReserva(Long usuarioId, Long pistaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        //Validación de nulos y numeros negativos
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El id del usuario no es válido.");
        }

        if (pistaId == null || pistaId <= 0) {
            throw new IllegalArgumentException("El id de la pista no es válido.");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }

        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("La hora de inicio y fin son obligatorias.");
        }

        //La fecha no puede ser menor que la de hoy
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar en una fecha pasada.");
        }

        //La hora de inicio no puede ser anterior a la hora de fin
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        //Si es para hoy no puede ser una hora ya pasada
        if (fecha.equals(LocalDate.now()) && !horaFin.isAfter(LocalTime.now())) {
            throw new IllegalArgumentException("La reserva debe terminar en una hora futura.");
        }

        Usuario usuario = usuarioDao.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        Pista pista = pistaDao.buscarPorId(pistaId);
        if (pista == null) {
            throw new IllegalArgumentException("La pista no existe.");
        }

        if (!pista.isActiva()) {
            throw new IllegalArgumentException("La pista no está activa y no se puede reservar.");
        }

        boolean ocupada = reservaDao.existeReserva(pistaId, fecha, horaInicio, horaFin);
        if (ocupada) {
            throw new IllegalArgumentException("Ya existe una reserva para esa pista en ese tramo horario.");
        }

        //Si to-do es correcto se crea la reserva
        Reserva reserva = new Reserva(usuario, pista, fecha, horaInicio, horaFin, true);

        return reservaDao.guardar(reserva);
    }

    //Lista de las reservas de un usuario
    public List<Reserva> listarReservasUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El id del usuario no es válido.");
        }

        return reservaDao.listarPorUsuario(usuarioId);
    }

    //Lista de las proximas reservas de un usuario
    public List<Reserva> listarProximasReservas(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El id del usuario no es válido.");
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return reservaDao.listarPorUsuario(usuarioId).stream()
                .filter(Reserva::isActiva)
                .filter(reserva ->
                        reserva.getFecha().isAfter(hoy) ||
                                (reserva.getFecha().isEqual(hoy) && reserva.getHora_fin().isAfter(ahora))
                )
                .sorted(Comparator
                        .comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHora_inicio))
                .toList();
    }

    // Lista de reservas pasadas de un usuario
    public List<Reserva> listarHistorialReservas(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El id del usuario no es válido.");
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return reservaDao.listarPorUsuario(usuarioId).stream()
                .filter(reserva ->
                        !reserva.isActiva() ||
                                reserva.getFecha().isBefore(hoy) ||
                                (reserva.getFecha().isEqual(hoy) && !reserva.getHora_fin().isAfter(ahora))
                )
                .sorted(Comparator
                        .comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHora_inicio)
                        .reversed())
                .toList();
    }

    // Cancelamos una reserva cambiando el boolean de actica a false
    public boolean cancelarReserva(Long reservaId) {
        if (reservaId == null || reservaId <= 0) {
            throw new IllegalArgumentException("El id de la reserva no es válido.");
        }

        Reserva reserva = reservaDao.buscarPorId(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no existe.");
        }

        if (!reserva.isActiva()) {
            throw new IllegalArgumentException("La reserva ya estaba cancelada.");
        }

        reserva.setActiva(false);
        return reservaDao.actualizar(reserva);
    }

    //Buscar reserva por id
    public Reserva buscarPorId(Long reservaId) {
        if (reservaId == null || reservaId <= 0) {
            throw new IllegalArgumentException("El id de la reserva no es válido.");
        }

        return reservaDao.buscarPorId(reservaId);
    }
}
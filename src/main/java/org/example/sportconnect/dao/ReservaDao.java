package org.example.sportconnect.dao;

import org.example.sportconnect.model.Reserva;
import org.example.sportconnect.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class ReservaDao {

    /**
     * Guarda la nueva reserva en la base de datos
     * @param reserva la reserva que se quiere guardar
     * @return true si se guarda correctamente, false si ocurre algún error
     */
    public boolean guardar(Reserva reserva) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(reserva);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error al guardar la reserva: " + e.getMessage());
            return false;
        }
    }

    /**
     * Para editar una reserva
     * @param reserva la reserva con los datos actualizados
     * @return true si se actualiza correctamente, false si ocurre algún error
     */
    public boolean actualizar(Reserva reserva) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(reserva);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error al editar la reserva: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una reserva de la base de datos
     * Primero se busca la reserva por su id y, si existe, se elimina
     * @param reserva la reserva que se quiere eliminar
     * @return true si se elimina correctamente, false si ocurre algún error
     */
    public boolean eliminar(Reserva reserva) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Reserva reservaBD = session.get(Reserva.class, reserva.getId());
            if (reservaBD != null) {
                session.remove(reservaBD);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error al eliminar la reserva: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una reserva por id
     * @param id el id de la reserva
     * @return la reserva encontrada, o null si no existe o ocurre un error
     */
    public Reserva buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reserva.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar la reserva: " + e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve una lista de todas las reservas
     * @return una lista de reservas; si ocurre un error, devuelve una lista vacía
     */
    public List<Reserva> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reserva", Reserva.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar las reservas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve todas las reservas de un usuario por su id
     * @param usuarioId el id del usuario
     * @return una lista de reservas del usuario; si ocurre un error, devuelve una lista vacía
     */
    public List<Reserva> listarPorUsuario(Long usuarioId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Reserva r WHERE r.usuario.id = :usuarioId",
                    Reserva.class
            ).setParameter("usuarioId", usuarioId).list();
        } catch (Exception e) {
            System.err.println("Error al listar las reservas por usuario: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve todas las reservas de una pista
     * @param pistaId el id de la pista
     * @return una lista de reservas de la pista; si ocurre un error, devuelve una lista vacía
     */
    public List<Reserva> listarPorPista(Long pistaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Reserva r WHERE r.pista.id = :pistaId",
                    Reserva.class
            ).setParameter("pistaId", pistaId).list();
        } catch (Exception e) {
            System.err.println("Error al listar las reservas por pista: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve todas las reservas de una una fecha que especifiquemos
     * @param fecha la fecha que se quiere consultar
     * @return una lista de reservas de esa fecha; si ocurre un error, devuelve una lista vacía
     */
    public List<Reserva> listarPorFecha(LocalDate fecha) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Reserva r WHERE r.fecha = :fecha",
                    Reserva.class
            ).setParameter("fecha", fecha).list();
        } catch (Exception e) {
            System.err.println("Error al listar reservas por fecha: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Comprueba si ya existe una reserva para una pista, una fecha y una hora concretas
     * La usamos para antes de crear una reserva, para no crear una reserva que ya exista
     * @param pistaId el id de la pista
     * @param fecha la fecha de la reserva
     * @param hora la hora de la reserva
     * @return true si ya existe una reserva con esos datos, false si no existe o ocurre un error
     */
    public boolean existeReserva(Long pistaId, LocalDate fecha, String hora) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long total = session.createQuery(
                            "SELECT COUNT(r) FROM Reserva r " +
                                    "WHERE r.pista.id = :pistaId " +
                                    "AND r.fecha = :fecha " +
                                    "AND r.hora = :hora",
                            Long.class
                    )
                    .setParameter("pistaId", pistaId)
                    .setParameter("fecha", fecha)
                    .setParameter("hora", hora)
                    .uniqueResult();

            return total != null && total > 0;

        } catch (Exception e) {
            System.err.println("Error al comprobar disponibilidad de reserva: " + e.getMessage());
            return false;
        }
    }
}
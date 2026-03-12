package org.example.sportconnect.dao;

import org.example.sportconnect.model.Pista;
import org.example.sportconnect.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PistaDao {

    /**
     * Crear una nueva pista en la base de datos
     * @param pista la pista que se quiere guardar
     * @return true si se guarda correctamente, false si ocurre algún error
     */
    public boolean guardar(Pista pista) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(pista);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al guardar la pista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Editar una pista
     * @param pista la pista con los datos actualizados
     * @return true si se actualiza correctamente, false si ocurre algún error
     */
    public boolean actualizar(Pista pista) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(pista);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al editar la pista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una pista buscandola por la id
     * @param pista la pista que se quiere eliminar
     * @return true si se elimina correctamente, false si ocurre algún error
     */
    public boolean eliminar(Pista pista) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Pista pistaBD = session.get(Pista.class, pista.getId());

            // Si existe, la eliminamos
            if (pistaBD != null) {
                session.remove(pistaBD);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al eliminar la pista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una pista por su id
     * @param id el id de la pista
     * @return la pista encontrada, o null si no existe o ocurre algún error
     */
    public Pista buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pista.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar la pista: " + e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve una lista con todas las pistas
     * @return una lista de pistas; si ocurre un error, devuelve una lista vacía
     */
    public List<Pista> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pista", Pista.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar las pistas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Para ver las pistas activas
     * @return una lista de pistas activas; si ocurre un error, devuelve una lista vacía
     */
    public List<Pista> listarActivas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Pista p WHERE p.activa = true",
                    Pista.class
            ).list();
        } catch (Exception e) {
            System.err.println("Error al listar las pistas activas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve una lista con las pistas con un deporte concreto
     * @param deporteId el id del deporte
     * @return una lista de pistas del deporte; si ocurre un error, devuelve una lista vacía
     */
    public List<Pista> listarPorDeporte(Long deporteId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Pista p WHERE p.deporte.id = :deporteId",
                    Pista.class
            ).setParameter("deporteId", deporteId).list();
        } catch (Exception e) {
            System.err.println("Error al listar las pistas por deporte: " + e.getMessage());
            return List.of();
        }
    }
}
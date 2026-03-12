package org.example.sportconnect.dao;

import org.example.sportconnect.model.Deporte;
import org.example.sportconnect.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DeporteDao {

    /**
     * Para crear un nuevo deporte en la base de datos
     * @param deporte el deporte que se quiere guardar
     * @return true si se guarda correctamente, false si ocurre algún error
     */
    public boolean guardar(Deporte deporte) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(deporte);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al guardar el deporte: " + e.getMessage());
            return false;
        }
    }

    /**
     * Editar un deporte que ya exista
     * @param deporte el deporte con los datos actualizados
     * @return true si se actualiza correctamente, false si ocurre algún error
     */
    public boolean actualizar(Deporte deporte) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.merge(deporte);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al editar el deporte: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un deporte buscandolo por su ip
     * @param deporte el deporte que se quiere eliminar
     * @return true si se elimina correctamente, false si ocurre algún error
     */
    public boolean eliminar(Deporte deporte) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Deporte deporteBD = session.get(Deporte.class, deporte.getId());
            if (deporteBD != null) {
                session.remove(deporteBD);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al eliminar el deporte: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un deporte por su id
     * @param id el id del deporte
     * @return el deporte encontrado, o null si no existe o ocurre un error
     */
    public Deporte buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Deporte.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar el deporte: " + e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve una lista con todos los deportes de la base de datos
     * @return una lista de deportes; si ocurre un error, devuelve una lista vacía
     */
    public List<Deporte> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Deporte", Deporte.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar los deportes: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca un deporte por su nombre
     * @param nombre el nombre del deporte
     * @return el deporte encontrado, o null si no existe o ocurre un error
     */
    public Deporte buscarPorNombre(String nombre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Deporte d WHERE d.nombre = :nombre",
                    Deporte.class
            ).setParameter("nombre", nombre).uniqueResult();
        } catch (Exception e) {
            System.err.println("Error al buscar el deporte por nombre: " + e.getMessage());
            return null;
        }
    }
}
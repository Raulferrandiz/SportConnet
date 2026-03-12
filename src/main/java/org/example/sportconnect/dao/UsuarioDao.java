package org.example.sportconnect.dao;

import org.example.sportconnect.model.Usuario;
import org.example.sportconnect.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UsuarioDao {

    /**
     * Guarda un usuario nuevo en la base de datos
     * @param usuario el usuario a guardar
     * @return true si se ha guardado correctamente, false si hubo error
     */
    public boolean guardar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(usuario);

            tx.commit();
            return true;

        } catch (Exception e) {
            // Si algo falla, deshacemos los cambios
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al guardar el usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Para editar un usuario
     * @param usuario el usuario con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean actualizar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // merge funciona mejor con objetos desacoplados
            session.merge(usuario);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al editar el usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario
     * @param usuario el usuario a eliminar
     * @return true si se eliminó correctamente, false si hubo error
     */
    public boolean eliminar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.remove(usuario);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario por su id
     * @param id id del usuario
     * @return el usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Usuario.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca un usuario por email
     * @param email email del usuario
     * @return el usuario encontrado o null si no existe
     */
    public Usuario buscarPorEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Comprueba si existe un usuario con ese email
     * para comprobar antes de registrar un nuevo usuario
     * @param email email a comprobar
     * @return true si existe, false si no existe
     */
    public boolean existePorEmail(String email) {
        return buscarPorEmail(email) != null;
    }

    /**
     * Devuelve todos los usuarios
     * @return lista de usuarios
     */
    public List<Usuario> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario", Usuario.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca un usuario por email y password
     * Para el login
     * @param email email del usuario
     * @param password contraseña del usuario
     * @return usuario encontrado o null si no coincide
     */
    public Usuario login(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Usuario u WHERE u.email = :email AND u.password = :password",
                            Usuario.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println("Error en login de usuario: " + e.getMessage());
            return null;
        }
    }
}
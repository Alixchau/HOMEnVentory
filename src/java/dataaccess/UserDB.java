package dataaccess;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.Role;
import models.User;

/**
 *
 * @author Alix
 */
public class UserDB {

    public Collection<User> getAll() throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Collection<User> users = em.createNamedQuery("User.findAll", User.class).getResultList();
            return users;
        } finally {
            em.close();
        }
    }

    public Collection<User> getAllActive() throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            Collection<User> activeUsers = (Collection<User>) em.createNamedQuery("User.findByActive").setParameter("active", true).getResultList();
            return activeUsers;
        } finally {
            em.close();
        }
    }

    public User get(String email) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            User user = em.find(User.class, email);
            return user;
        } finally {
            em.close();
        }
    }

    public void insert(User user) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            Role role = user.getRole();//get the role that the user belongs to
            role.getUserCollection().add(user); //add the user to the userlist of the role

            em.persist(user);
            em.merge(role);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void update(User user) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(user);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void delete(User user) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Role role = user.getRole();//get the role that the user belongs to
            role.getUserCollection().remove(user); //add the user to the userlist of the role

            em.remove(em.merge(user));
            em.merge(role);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public User getByResetPasswordUUID(String uuid) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            User user = (User) em.createNamedQuery("User.findByResetPasswordUuid").setParameter("resetPasswordUuid", uuid).getSingleResult();
            return user;
        } finally {
            em.close();
        }
    }

    public User getByRegisterUUID(String uuid) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            User user = (User) em.createNamedQuery("User.findByRegisterUuid").setParameter("registerUuid", uuid).getSingleResult();
            return user;
        } finally {
            em.close();
        }
    }
}

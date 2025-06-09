package site.pokemons.edpproject.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import org.mindrot.jbcrypt.BCrypt;
import site.pokemons.edpproject.model.User;
import site.pokemons.edpproject.model.db.JpaPersistenceUnit;
import site.pokemons.edpproject.session.SessionContext;

import java.time.LocalDateTime;

public class UserService {

    public boolean loginUser(String username, String password) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username).getSingleResult();
            if (user == null) throw new EntityNotFoundException("User " + username + " not found");

            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                SessionContext.setLoggedInUserId(user.getUserId());
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean registerUser(String username, String password, String email, String name, String surname) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        Long accountSum = em.createQuery("SELECT COUNT(a) FROM User a WHERE a.username = :username", Long.class)
                .setParameter("username", username).getResultStream().findFirst().orElse(0L);
        if (accountSum > 0) throw new IllegalArgumentException("User " + username + " already exists");

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User(username, hashedPassword, email, name, surname, LocalDateTime.now(), "USER");
            em.persist(user);

            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
        return false;
    }

    public User getAccountById(long id) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user == null) throw new EntityNotFoundException("Account with id " + id + " not found");
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
        return null;
    }

    public void changeProfileInformation(String email, String username, String name, String surname) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        long userId = SessionContext.getLoggedInUserId();
        User user = em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId).getSingleResult();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if(!email.isEmpty()) user.setEmail(email);
            if(!username.isEmpty()) user.setUsername(username);
            if(!name.isEmpty()) user.setName(name);
            if(!email.isEmpty()) user.setSurname(surname);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
    }

    public void changePassword(String password, String newPassword, String newPasswordSecond) {
        if (!newPassword.equals(newPasswordSecond)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        long userId = SessionContext.getLoggedInUserId();
        User user = em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId).getSingleResult();

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Actual password is wrong.");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            user.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (tx.isActive()) tx.commit();
            em.close();
        }
    }
}

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
    private static UserService instance;

    private UserService() {}

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean loginUser(String username, String password) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username).getSingleResult();
            if (user == null) throw new EntityNotFoundException("User " + username + " not found");

            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                SessionContext.setLoggedInUserId(user.getUserId());
                SessionContext.setLoggedInUserEmail(user.getEmail());
                SessionContext.setLoggedInUserRole(user.getRole());
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

            SessionContext.setLoggedInUserId(user.getUserId());
            SessionContext.setLoggedInUserEmail(user.getEmail());
            SessionContext.setLoggedInUserRole(user.getRole());
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
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
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
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
            if (!email.isEmpty()) user.setEmail(email);
            if (!username.isEmpty()) user.setUsername(username);
            if (!name.isEmpty()) user.setName(name);
            if (!email.isEmpty()) user.setSurname(surname);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void changePassword(String password, String newPassword, String newPasswordSecond) {
        EntityManager em = JpaPersistenceUnit.getEntityManager();

        long userId = SessionContext.getLoggedInUserId();
        User user = em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId).getSingleResult();

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Aktualne hasło jest błędne.");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            user.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}

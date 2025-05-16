package site.pokemons.edpproject.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mindrot.jbcrypt.BCrypt;
import site.pokemons.edpproject.model.Account;
import site.pokemons.edpproject.session.SessionContext;

public class UserService {

    public boolean loginUser(String username, String password){
        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            Account account = em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class)
                            .setParameter("username", username).getResultStream().findFirst().orElse(null);
            if(account == null) return false;

            if(BCrypt.checkpw(password, account.getHashPassword())){
                SessionContext.setLoggedInUserId(account.getId());
                return true;
            }

            return false;

        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        }finally{
            tx.commit();
            em.close();
        }
        return false;
    }

    public boolean registerUser(String username, String password){
        EntityManager em = JpaPersistenceUnit.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            Long accountSum = em.createQuery("SELECT COUNT(a) FROM Account a WHERE a.username = :username", Long.class)
                    .setParameter("username", username).getSingleResult();
            if(accountSum > 0) return false;

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Account account = new Account(username, hashedPassword);
            em.persist(account);

            return true;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        }finally{
            tx.commit();
            em.close();
        }
        return false;
    }
}

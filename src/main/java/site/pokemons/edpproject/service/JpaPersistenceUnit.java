package site.pokemons.edpproject.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaPersistenceUnit {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close(){
        emf.close();
    }
}

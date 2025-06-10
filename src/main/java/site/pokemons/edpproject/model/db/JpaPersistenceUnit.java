package site.pokemons.edpproject.model.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JpaPersistenceUnit {
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        if (emf == null) {
            Properties props = new Properties();
            try (InputStream input = JpaPersistenceUnit.class.getClassLoader().getResourceAsStream("config.properties")) {
                props.load(input);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Nie udało się wczytać config.properties");
            }

            Map<String, Object> configOverrides = new HashMap<>();
            for (String key : props.stringPropertyNames()) {
                configOverrides.put(key, props.getProperty(key));
            }

            emf = Persistence.createEntityManagerFactory("myPersistenceUnit", configOverrides);
        }
        return emf.createEntityManager();
    }

//    public static EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public static void close(){
        emf.close();
    }
}

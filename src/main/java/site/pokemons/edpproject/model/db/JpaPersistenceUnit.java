package site.pokemons.edpproject.model.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class JpaPersistenceUnit {
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        if (emf == null) {
            Map<String, Object> configOverrides = new HashMap<>();
            Configurations configs = new Configurations();

            try {
                PropertiesConfiguration config = configs.properties("config.properties");

                for (Iterator<String> it = config.getKeys(); it.hasNext(); ) {
                    String key = it.next();
                    configOverrides.put(key, config.getString(key));
                }

                emf = Persistence.createEntityManagerFactory("myPersistenceUnit", configOverrides);
            } catch (ConfigurationException e) {
                e.printStackTrace();
                throw new RuntimeException("Błąd ładowania konfiguracji z Apache Commons Configuration");
            }
        }

        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}



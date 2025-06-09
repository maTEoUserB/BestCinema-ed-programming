package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.EmailService;

public class EmailServiceSingleton {
    private static EmailService instance;

    private EmailServiceSingleton() {}

    public static synchronized EmailService getInstance() {
        if (instance == null) {
            return new EmailService();
        }
        return instance;
    }
}

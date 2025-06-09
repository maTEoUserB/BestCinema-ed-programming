package site.pokemons.edpproject.service.serviceSingleton;

import site.pokemons.edpproject.service.UserService;

public class UserServiceSingleton {
    private static UserService instance;

    private UserServiceSingleton() {}

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
}

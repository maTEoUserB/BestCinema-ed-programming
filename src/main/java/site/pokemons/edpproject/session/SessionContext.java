package site.pokemons.edpproject.session;

import lombok.Getter;
import lombok.Setter;

public class SessionContext {
    @Getter
    @Setter
    private static Long loggedInUserId;

    public static void clear(){
        loggedInUserId = null;
    }
}

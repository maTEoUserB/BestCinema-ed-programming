package site.pokemons.edpproject.validator;

public class InputValidator {
    private static InputValidator instance;

    private InputValidator() {}

    public static InputValidator getInstance() {
        if (instance == null) {
            instance = new InputValidator();
        }
        return instance;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

        return password != null && password.matches(regex);
    }

    public boolean isValidUsername(String username) {
        String regex = "^(?=.*\\d).{8,}$";
        return username != null && username.matches(regex);
    }
}

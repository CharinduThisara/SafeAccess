package Users;
public class Patient extends User {

    private static final int MAX_PRIV = 1;

    public Patient(String username, String password, int userType, int privLvl) {

        super(username, password, userType, Math.min(privLvl,MAX_PRIV));
    }
}

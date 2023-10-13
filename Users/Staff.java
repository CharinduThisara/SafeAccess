package Users;

public class Staff extends User {

    private static final int MAX_PRIV = 2;

    public Staff(String username, String password, int userType, int privLvl) {

        super(username, password, userType, Math.min(privLvl,MAX_PRIV));

    }
}

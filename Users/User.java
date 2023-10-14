package Users;
// User class to store user information
public abstract class User {
    private int ID;
    private String username;
    private String password;
    private int userType;
    private int privLvl;

    public User(String username, String password, int userType, int privLvl) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.privLvl  = privLvl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserType() {
        return userType;
    }

    public int getPrivLvl() {
        return privLvl;
    }
}


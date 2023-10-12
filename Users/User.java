package Users;
// User class to store user information
public abstract class User {
    private String username;
    private String password;
    private int userType;
    private int privLvlIntegrity;
    private int privLvlConfidentiality;

    public User(String username, String password, int userType, int privLvlIntegrity,int privLvlConfidentiality) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.privLvlIntegrity = privLvlIntegrity;
        this.privLvlConfidentiality = privLvlConfidentiality;
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

    public int getPrivLvlIntegrity() {
        return privLvlIntegrity;
    }

    public int getPrivLvlConfidentiality() {
        return privLvlConfidentiality;
    }
}

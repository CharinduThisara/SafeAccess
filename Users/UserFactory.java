package Users;
public abstract class UserFactory {
    private static Patient getPatient(String username, String password, int userType, int privLvl){
        return new Patient(username, password, userType,privLvl);
    }

    private static Staff getStaff(String username, String password, int userType, int privLvl){
        return new Staff(username, password, userType,privLvl);
    }

    public static User getUser(String username, String password, int userType, int privLvl){
        if (userType == 0){
            return getPatient(username, password, userType, privLvl);
        }
        else if(userType == 1){
            return getStaff(username, password, userType, privLvl);
        }
        else{
            return null;
        }
    }
}

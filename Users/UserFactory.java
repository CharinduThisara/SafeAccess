package Users;
public abstract class UserFactory {
    private static Patient getPatient(String username, String password, int userType, int privLvlIntegrity, int privLvlConfidentiality){
        return new Patient(username, password, userType,privLvlIntegrity,privLvlConfidentiality);
    }

    private static Staff getStaff(String username, String password, int userType, int privLvlIntegrity, int privLvlConfidentiality){
        return new Staff(username, password, userType,privLvlIntegrity,privLvlConfidentiality);
    }

    public static User getUser(String username, String password, int userType, int privLvlIntegrity, int privLvlConfidentiality){
        if (userType == 0){
            return getPatient(username, password, userType, privLvlIntegrity, privLvlConfidentiality);
        }
        else if(userType == 1){
            return getStaff(username, password, userType, privLvlIntegrity, privLvlConfidentiality);
        }
        else{
            return null;
        }
    }
}

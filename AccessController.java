import java.util.Arrays;
import java.util.HashMap;

import Users.UserFactory;
import Users.User;

public class AccessController {
    private static final String USRCONFPATH = "Files/userConf.csv";
    private static final String DATAPATH = "Files/data.csv";

    //user data stored in a HashMap
    private final HashMap<String, User> users;
    //data records stored in a HashMap
    private final HashMap<Integer, DataRecord> dataRecords;

    private final FileController fileController;

    // Constants for user types
    public static final int PATIENT = 0;
    public static final int HOSPITAL_STAFF = 1;

    // Constants for privilege levels (Integrity)
    public static final int LOW_PRIVILEGE = 0;
    public static final int MEDIUM_PRIVILEGE = 1;
    public static final int HIGH_PRIVILEGE = 2;

    // Constants for secret levels (Confidentiality)
    public static final int PUBLIC = 0;
    public static final int CONFIDENTIAL = 1;
    public static final int SECRET = 2;

    public AccessController(){
        this.users = new HashMap<>();
        this.dataRecords = new HashMap<>();
        this.fileController = new FileController();
        loadUsers();
        loadData();
    }
    public void loadUsers(){

        String[] userData = this.fileController.readUserConfig(USRCONFPATH);

        for (String data : userData){
            User user = UserFactory.getUser(
                data.split(",")[0].strip(), 
                data.split(",")[1].strip(),
                Integer.parseInt(data.split(",")[2].strip()),
                Integer.parseInt(data.split(",")[3].strip()),
                Integer.parseInt(data.split(",")[4].strip()));
            users.put(user.getUsername(), user);
        }
    }

    public void loadData(){

        String[] Data = this.fileController.readUserConfig(DATAPATH);

        for (String record : Data){
            String[] temp = record.split("--");
            DataRecord dataRecord = new DataRecord(
                Integer.parseInt(temp[0].strip()), 
                Arrays.stream(temp[1].strip().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray(),
                temp[2].strip(),
                temp[3].strip(),
                temp[4].strip(),
                temp[5].strip());
            dataRecords.put(dataRecord.getId(),dataRecord);
        }
    }

    public DataRecord getDataRecord(int id) {
        return dataRecords.get(id);
    }

    public User getUser(String username) {
        return users.get(username);
    }

   /*  private String formatData(String username, String password, int userType, int privLvlIntegrity,int privLvlConfidentiality){
        return new String(null, privLvlIntegrity, privLvlConfidentiality, null)
    }
 */
    

    public void addUser(String username, String password, int userType, int privLvlIntegrity,int privLvlConfidentiality){

        User user = UserFactory.getUser(username,password,userType,privLvlIntegrity,privLvlConfidentiality);
        users.put(user.getUsername(), user);
    }

    public String getAllData(User user){
        
        dataRecords.forEach((key, dataRecord) -> {
            if (user != null && dataRecord != null) {
                if (user.getUserType() == PATIENT) {
                    // Patients can read their own data records (if sensitivity allows)
                    if (dataRecord.getSensitivityLevels()[0] <= MEDIUM_PRIVILEGE) {
                        System.out.println("Accessing patient data:\n");
                        System.out.format("%-5s %-30s%-25s%-25s%s\n","ID","Personal_Details", "Sickness","Drug_Prescription","Lab_Test_Prescription");
                        System.out.println("--------------------------------------------------------------------------");
                        System.out.format("%-5d%-30s%-25s%-25s%s\n", dataRecord.getId(),dataRecord.getPersonalDetails(),dataRecord.getSicknessDetails(),dataRecord.getDrugPrescriptions(),dataRecord.getLabTestPrescriptions());
                        dataRecord.printData();
                
                    } else {
                        System.out.println("Access denied due to sensitivity level.");
                    }
                } else if (user.getUserType() == HOSPITAL_STAFF) {
                    // Hospital staff can access data based on their privilege level
                    if (user.getPrivLvlIntegrity() >= dataRecord.getSensitivityLevels()[0]) {
                        System.out.println("Accessing hospital staff data:");
                        System.out.format("%-5s %-30s%-25s%-25s%s\n","ID","Personal_Details", "Sickness","Drug_Prescription","Lab_Test_Prescription");
                        System.out.println("--------------------------------------------------------------------------");
                        System.out.format("%-5d%-30s%-25s%-25s%s\n", dataRecord.getId(),dataRecord.getPersonalDetails(),dataRecord.getSicknessDetails(),dataRecord.getDrugPrescriptions(),dataRecord.getLabTestPrescriptions());
                        dataRecord.printData();
                    } else {
                        System.out.println("Access denied due to privilege level.");
                    }
                }
            }
        });
        return new String();
        }
}


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        /* DataRecord dataRecord1 = new DataRecord(
            1, 
            MEDIUM_PRIVILEGE,
            "Name: John Doe, Age: 30, Gender: Male",
            "Diagnosis: Flu, Treatment: Rest and hydration",
            "Medication: Paracetamol, Dosage: 500mg",
            "Lab Tests: Blood Test, Urine Test"
        );
        dataRecords.put(dataRecord1.getId(), dataRecord1);

        DataRecord dataRecord2 = new DataRecord(
            2, 
            HIGH_PRIVILEGE, 
            "Name: Jane Smith, Age: 45, Gender: Female",
            "Diagnosis: COVID-19, Treatment: Isolation and monitoring",
            "Medication: Antiviral drugs, Lab Tests: COVID-19 test",
            "Lab Tests: Blood Test"
        );
        dataRecords.put(dataRecord2.getId(), dataRecord1);
      */   
    }

    public DataRecord getDataRecord(int id) {
        return dataRecords.get(id);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

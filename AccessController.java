import java.util.HashMap;

import Users.User;
import Users.UserFactory;

public class AccessController {
    private static final String USRCONFPATH = "Files/useConf.csv";
    private static final String DATAPATH = "Files/data.csv";

    //user data stored in a HashMap
    private HashMap<String, User> users = new HashMap<>();
    
    //data records stored in a HashMap
    private HashMap<Integer, DataRecord> dataRecords = new HashMap<>();

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
        loadUsers();
        loadData();
    }
    public void loadUsers(){
        User user1 = UserFactory.getUser("user1", "password1", PATIENT, LOW_PRIVILEGE,SECRET);
        users.put(user1.getUsername(), user1);
        User user2 = UserFactory.getUser("user2", "password2", HOSPITAL_STAFF, HIGH_PRIVILEGE,PUBLIC);
        users.put(user2.getUsername(), user2);
    }

    public void loadData(){
        DataRecord dataRecord1 = new DataRecord(
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
        
    }

    public DataRecord getDataRecord(int id) {
        return dataRecords.get(id);
    }

}

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
                Integer.parseInt(data.split(",")[3].strip()));
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
  
    public void addUser(String username, String password, int userType, int privLvl){

        User user = UserFactory.getUser(username,password,userType,privLvl);
        users.put(user.getUsername(), user);
    }

    private void printRecord(String[] array){
        String[] personalData = array[1].split(",");
        String name;
        String age;
        String gender;

        if (personalData.length>1){
            name = personalData[0].strip();
            age  = personalData[1].strip();
            gender = personalData[2].strip();
        }
        else{
            name = personalData[0].strip();
            age = "";
            gender = "";
        }
        
        System.out.format("%5s |%-25s |%20s |%20s |%s\n",array[0], name,array[2],array[3],array[4]);
        System.out.format("%5s |%-25s |%20s |%20s |%s\n","", age,"","","");
        System.out.format("%5s |%-25s |%20s |%20s |%s\n","", gender,"","","");
        System.out.println("---------------------------------------------------------------------------------------------------");
        
    }

    public void getAllData(User user){
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.format("%5s |%25s |%20s |%20s |%s\n","ID","Personal", "Sickness","Drug","Lab_Test");
        System.out.format("%5s |%25s |%20s |%20s |%s\n","","Details", "","Prescription","Prescription");
        System.out.println("---------------------------------------------------------------------------------------------------");
                    
        dataRecords.forEach((key, dataRecord) -> {
            if (user != null && dataRecord != null) {
                if (user.getUserType() == PATIENT) {
                    // Patients can read their own data records (if sensitivity allows)
                    if (dataRecord.getSensitivityLevels()[0] <= MEDIUM_PRIVILEGE) {
                        dataRecord.printData();
                        //printRecord(new String[4]);
                    } else {
                        System.out.println("Access denied due to sensitivity level.");
                    }
                } else if (user.getUserType() == HOSPITAL_STAFF) {
                    // Hospital staff can access data based on their privilege level

                    String[] array = new String[5];
                    int k=0;
                    array[k++] = Integer.toString(dataRecord.getId());
                    array[k++] = dataRecord.getPersonalDetails();
                    array[k++] = dataRecord.getSicknessDetails();
                    array[k++] = dataRecord.getDrugPrescriptions();
                    array[k++] = dataRecord.getLabTestPrescriptions();
                    for (int i=1; i<5 ; i++ ){
                        if (user.getPrivLvl() < dataRecord.getSensitivityLevels()[i-1]) {
                            array[i]= "Access Denied";
                        }
                    }
                    printRecord(array);
                }
            }
        });
        }
}


import java.util.Arrays;
import java.util.HashMap;

import Users.UserFactory;
import Users.User;

public class AccessController {
    private static final String USRCONFPATH = "Files/userConf.csv";
    private static final String DATAPATH = "Files/data.csv";

    public static final int READ = 0;
    public static final int WRITE = 1;

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

        users.clear();

        String[] userData = this.fileController.readFile(USRCONFPATH);

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

        dataRecords.clear();

        String[] Data = this.fileController.readFile(DATAPATH);

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
  
    public void addUser(User curUser, String username, String password, int userType, int privLvl){
        
        if (!checkAccess(curUser,2,WRITE))
            return;

        User user = UserFactory.getUser(username,password,userType,privLvl);
        users.put(user.getUsername(), user);

        String[] Lines = {user.getUsername()+","+user.getPassword()+","+user.getUserType()+","+user.getPrivLvl()};

        fileController.writeFile(Lines, USRCONFPATH);

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

    public void writeRecord(User user, String[] data, int[] sensitivities){
        try{
            if (!checkAccess(user, 0, WRITE))
                return;

            if(!isValid(data,sensitivities))
                return;
            
            String[] personalDetail = {data[0],data[1],data[2]};
            String[] temp = Arrays.copyOfRange(data, 3, 6);
            data[0] = String.join(",", personalDetail);
            data[1] = String.join("--",temp);
            String line = data[0]+"--"+data[1];

            int i = 0;
            line = Integer.toString(sensitivities[i++])+","+
                   Integer.toString(sensitivities[i++])+","+
                   Integer.toString(sensitivities[i++])+","+
                   Integer.toString(sensitivities[i++])+"--"+
                   line;
            
            line = Integer.toString(DataRecord.getRecordCount()+1)+"--"+line;

            System.out.println(DataRecord.getRecordCount());
            
            String[] Lines = {line};
            
            fileController.writeFile(Lines, DATAPATH);
            loadData();

        }catch(Exception e){
            System.out.println("Data write failed");
        }
    }

    private boolean isValid(String[] data, int[] sensitivities) {
        return true;
    }
    // ROLE BASED REFERENCE MONITOR ---- THIS CHECKS SENSITIVITY + PRIVILEGE LEVEL
    public boolean checkAccess(User user, int sensitivity_level, int action){
        if (user.getUserType() == PATIENT){
            if (action == READ && sensitivity_level == PUBLIC)
                return true;
            else
                return false;
        }
        else if (user.getUserType() == HOSPITAL_STAFF){

            if (action == READ && sensitivity_level == PUBLIC)
                return true;
            else if(action == READ && sensitivity_level == CONFIDENTIAL && user.getPrivLvl() > LOW_PRIVILEGE)
                return true;
            else if(action == READ && sensitivity_level == SECRET && user.getPrivLvl() > MEDIUM_PRIVILEGE)
                return true;
            else if(action == WRITE && user.getPrivLvl() == HIGH_PRIVILEGE)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public void getAllData(User user){
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.format("%5s |%-25s |%20s |%20s |%s\n","ID","Personal", "Sickness","Drug","Lab_Test");
        System.out.format("%5s |%-25s |%20s |%20s |%s\n","","Details", "","Prescription","Prescription");
        System.out.println("---------------------------------------------------------------------------------------------------");
                    
        dataRecords.forEach((key, dataRecord) -> {
            if (user != null && dataRecord != null) {
                    String[] array = new String[5];
                    int k=0;
                    array[k++] = Integer.toString(dataRecord.getId());
                    array[k++] = dataRecord.getPersonalDetails();
                    array[k++] = dataRecord.getSicknessDetails();
                    array[k++] = dataRecord.getDrugPrescriptions();
                    array[k++] = dataRecord.getLabTestPrescriptions();
                    for (int i=1; i<5 ; i++ ){
                        if (!checkAccess(user, dataRecord.getSensitivityLevels()[i-1], READ)) {
                            array[i]= "Access Denied";
                        }
                    }
                    printRecord(array);
                }
            });
        }
}


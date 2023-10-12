import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import Users.User;
import Users.UserFactory;

import java.io.Console;

public class MedicalDataProcessing {

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

    // Sample user data stored in a HashMap
    private static HashMap<String, User> users = new HashMap<>();
    
    // Sample data records stored in a data structure
    private static HashMap<Integer, DataRecord> dataRecords = new HashMap<>();

    private User currentUser = null;

    

   

    // Get a user by username
    private static User getUser(String username) {
        return users.get(username);
    }


    

    // Hash a password using MD5 (insecure, for demonstration purposes)
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

    public boolean login(){

        System.out.println("      ___           ___           ___           ___     \n" + //
                "     /\\__\\         /\\  \\         /\\__\\         /\\__\\    \n" + //
                "    /:/ _/_       /::\\  \\       /:/ _/_       /:/ _/_   \n" + //
                "   /:/ /\\  \\     /:/\\:\\  \\     /:/ /\\__\\     /:/ /\\__\\  \n" + //
                "  /:/ /::\\  \\   /:/ /::\\  \\   /:/ /:/  /    /:/ /:/ _/_ \n" + //
                " /:/_/:/\\:\\__\\ /:/_/:/\\:\\__\\ /:/_/:/  /    /:/_/:/ /\\__\\\n" + //
                " \\:\\/:/ /:/  / \\:\\/:/  \\/__/ \\:\\/:/  /     \\:\\/:/ /:/  /\n" + //
                "  \\::/ /:/  /   \\::/__/       \\::/__/       \\::/_/:/  / \n" + //
                "   \\/_/:/  /     \\:\\  \\        \\:\\  \\        \\:\\/:/  /  \n" + //
                "     /:/  /       \\:\\__\\        \\:\\__\\        \\::/  /   \n" + //
                "     \\/__/         \\/__/         \\/__/         \\/__/    \n" + //
                "");


        Console console = System.console();

        if (console == null) {
            System.err.println("Console not available. Exiting.");
            System.exit(1);
        }

        String username = console.readLine("Login > Username : ");
        System.out.print("");
        String password = new String(console.readPassword("      > Password : "));
        System.out.print("");

        User curUser = users.get(username);
        if (curUser == null){
            return false;
        }
        
        
        if (curUser.getPassword().equals(hashPassword(password))){
            currentUser = curUser;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        
        MedicalDataProcessing program = new MedicalDataProcessing();

        AccessController accessController = new AccessController();

        if(program.login()==false){
            System.out.println("Login Fail");
            return;
        }



        // Simulate access to data based on user privilege and data sensitivity
        User currentUser = program.currentUser;
        DataRecord dataRecord = accessController.getDataRecord(1);

        if (currentUser != null && dataRecord != null) {
            if (currentUser.getUserType() == PATIENT) {
                // Patients can read their own data records (if sensitivity allows)
                if (dataRecord.getSensitivityLevel() <= MEDIUM_PRIVILEGE) {
                    System.out.println("Accessing patient data:");
                    dataRecord.printData();
                } else {
                    System.out.println("Access denied due to sensitivity level.");
                }
            } else if (currentUser.getUserType() == HOSPITAL_STAFF) {
                // Hospital staff can access data based on their privilege level
                if (currentUser.getPrivLvlIntegrity() >= dataRecord.getSensitivityLevel()) {
                    System.out.println("Accessing hospital staff data:");
                    dataRecord.printData();
                } else {
                    System.out.println("Access denied due to privilege level.");
                }
            }
        }
    }

}

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import Users.User;

import java.io.Console;

public class MedicalDataProcessing {

    // Constants for user types
    public static final int PATIENT = 0;
    public static final int HOSPITAL_STAFF = 1;

    public static final int READ = 0;
    public static final int WRITE = 1;

    // Constants for privilege levels (Integrity)
    public static final int LOW_PRIVILEGE = 0;
    public static final int MEDIUM_PRIVILEGE = 1;
    public static final int HIGH_PRIVILEGE = 2;

    // Constants for secret levels (Confidentiality)
    public static final int PUBLIC = 0;
    public static final int CONFIDENTIAL = 1;
    public static final int SECRET = 2;

    private AccessController accessController = null;

    private User currentUser = null;

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

    public void viewData(User user){
        this.accessController.getAllData(user);
    }

    public void writeData(User user,Scanner scanner){
        if (!accessController.checkAccess(user,0, WRITE)){
            System.out.println("Write is not Allowed");
            return;
        }
        try{
            System.out.println("Enter Data....\n");
            System.out.print(
                "Personal Details:..\n"+//
                "Name: "
            );
            String[] Data = new String[6];
            int i = 0;
            
            Data[i++] = "Name: "+scanner.nextLine().strip();

            System.out.print(
                "Age: "
            );

            Data[i++] = "Age: "+scanner.nextLine().strip();

            System.out.print(
                "Gender:"
            );

            Data[i++] = "Gender: "+scanner.nextLine().strip();

            System.out.print(
                "sickness details      : "
            );

            Data[i++] = scanner.nextLine().strip();

            System.out.print(
                "drug prescriptions    : "
            );

            Data[i++] = scanner.nextLine().strip();

            System.out.print(
                "lab test prescriptions: "
            );

            Data[i++] = scanner.nextLine().strip();

            System.out.print(
                "Sensitivity Lvls(0-2) for\n"+//
                "for each category\n "+//
                "format --> 1,2,3,4\n"+//
                "Enter     : "
            );

            int[] sensitivities = new int[4];

            String[] sensitivitiesChr = scanner.nextLine().split(",");
            for (int j = 0; j < 4; j++) {
                sensitivities[j] = Integer.parseInt(sensitivitiesChr[j]);
            }

            accessController.writeRecord(user, Data, sensitivities);
        }
        catch(Exception e){
            System.out.println("Error in Writing Data");
        }

    }
    
    private void addUser(User user) {
        
        if (!accessController.checkAccess(user,2,WRITE)){
            return;
        }

        Console console = System.console();

        System.out.println("Enter User Data....\n");
        String[] Data = new String[4];
        int i = 0;
        
        Data[i++] = console.readLine("UserName           : ");
        Data[i++] = new String(console.readPassword("Password           : "));
        Data[i++] = new String(console.readPassword("re-enter Password  : "));

        if (!Data[i-1].equals(Data[i-2])){
            System.out.println("Passwords Do not Match");
            return;
        }

        i--;

        Data[i++] = console.readLine("UserType\n(0-patient/1-Staff): ");
        Data[i++] = console.readLine("Privilege LVL (0,1,2)   : ");
       
        i=0;
        accessController.addUser(user,Data[i++], hashPassword(Data[i++]),Integer.parseInt(Data[i++]),Integer.parseInt(Data[i++]));
    }
    public void init(){
        this.accessController = new AccessController();
    }

    private void logout(User user) {
        if (user!=null){
            this.currentUser = null;
            accessController.loadUsers();
            login();
        }
    }


    public boolean login(){
        try{
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

            User curUser = accessController.getUser(username);

            if (curUser == null){
                return false;
            }
            
            if (curUser.getPassword().equals(hashPassword(password))){
                currentUser = curUser;
                return true;
            }
    }
    catch(Exception e){
        System.out.println("error in login");
        return false;
    }
    return false;
    }

    public static void main(String[] args) {
        
        MedicalDataProcessing program = new MedicalDataProcessing();

        program.init();

        if(program.login()==false){
            System.out.println("Login Fail");
            return;
        }

        System.out.format("\nLogin Successfull\n\nWELCOME %s...!!\n\n",program.currentUser.getUsername());

        try (Scanner scanner = new Scanner(System.in)) {
            
            while(true){
            
                System.out.println(
                    "Select a Choice....\n"+//
                    "Write new Data(1)\n"+//
                    "Read All Data (2)\n"+//
                    "Add user      (3)\n"+//
                    "Logout        (4)\n"+//
                    "Exit          (99)"
                );

                System.out.print("Choice: ");
                String choice = scanner.nextLine();
                System.out.println("You entered: " + choice);

                switch (Integer.parseInt(choice)) {
                    case 1:
                        program.writeData(program.currentUser,scanner);
                        break;
                    case 2:
                        program.viewData(program.currentUser);
                        break;
                    case 3:
                        program.addUser(program.currentUser);
                        break;
                    case 4:
                        program.logout(program.currentUser);
                        break;
                    case 99:
                        System.out.println("\nExiting..........");
                        return;
                    default:
                        continue;
                }
            }
        }      
        catch (Exception e) {
            e.printStackTrace();
        }  
    }

}

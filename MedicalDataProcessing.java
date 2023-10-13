import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import Users.User;

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

    public String viewData(User user){
        return this.accessController.getAllData(user);
    }

    public void init(){
        this.accessController = new AccessController();
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

        User curUser = accessController.getUser(username);

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

        program.init();

        if(program.login()==false){
            System.out.println("Login Fail");
            return;
        }



        // Simulate access to data based on user privilege and data sensitivity
        User currentUser = program.currentUser;

        program.viewData(currentUser);
        
        
    }

}

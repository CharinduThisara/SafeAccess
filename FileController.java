import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {

    public static void writeuserConf(String[] userData, String fileName) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName)); //try with resources to auto close without suppressing exceptions
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Write user data
            for (String user : userData) {
                bufferedWriter.write(user);
                bufferedWriter.newLine();
            }

            System.out.println("User data has been written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] readUserConfig(String filePath) {
        try(FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // Read user data into a StringBuilder
            StringBuilder userData = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                userData.append(line).append("\n");
            }

            // Split the data into an array of user records
            String[] userRecords = userData.toString().split("\n");

            return userRecords;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
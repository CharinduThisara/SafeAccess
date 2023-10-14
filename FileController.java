import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {

    public void writeFile(String[] Data, String fileName) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName),true); //try with resources to auto close without suppressing exceptions
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Write user data
            for (String line : Data) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            System.out.println("Data has been written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] readFile(String filePath) {
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
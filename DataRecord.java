// DataRecord class to store data records
public class DataRecord {
    private int id;
    private int[] sensitivityLevels;
    private String personalDetails;
    private String sicknessDetails;
    private String drugPrescriptions;
    private String labTestPrescriptions;

    public DataRecord(int id, int[] sensitivityLevels, String personalDetails,String sicknessDetails, String drugPrescriptions, String labTestPrescriptions) {
        this.id = id;
        this.sensitivityLevels = sensitivityLevels;
        this.personalDetails = personalDetails;
        this.sicknessDetails = sicknessDetails;
        this.drugPrescriptions = drugPrescriptions;
        this.labTestPrescriptions = labTestPrescriptions;
    }

    public int getId() {
        return id;
    }

    public int[] getSensitivityLevels() {
        return sensitivityLevels;
    }

    public String getPersonalDetails() {
        return personalDetails;
    }

    public String getSicknessDetails() {
        return sicknessDetails;
    }

    public String getDrugPrescriptions() {
        return drugPrescriptions;
    }

    public String getLabTestPrescriptions() {
        return labTestPrescriptions;
    }
    
    // Print the data record
    public void printData() {
        System.out.println("Data Record #" + id);
        System.out.println("Sensitivity Levels: " + sensitivityLevels[0]+","+sensitivityLevels[1]+","+sensitivityLevels[2]+","+sensitivityLevels[3]);
        System.out.println("Personal Details: " + personalDetails);
        System.out.println("Sickness Details: " + sicknessDetails);
        System.out.println("Drug Prescriptions: " + drugPrescriptions);
        System.out.println("Lab Test Prescriptions: " + labTestPrescriptions);
    }
}
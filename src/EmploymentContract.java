public class EmploymentContract {
    private String contractId;
    private double monthlyRate;

    public EmploymentContract(String contractId, double monthlyRate) {
        this.contractId = contractId;
        this.monthlyRate = monthlyRate;
    }

    public void displayContractDetails() {
        System.out.println("   -> Contract ID: " + contractId + " | Rate: $" + monthlyRate);
    }
}
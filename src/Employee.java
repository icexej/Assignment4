public abstract class Employee implements Workable, Reportable {
    private String name;
    private int id;
    private int experienceYears;
    private double baseSalary;
    private EmploymentContract contract;

    public Employee() {
        this.name = "Unknown Employee";
        this.id = 0;
        this.experienceYears = 0;
        this.baseSalary = 500.0;
    }

    public Employee(String name, int id, int experienceYears, double baseSalary) {
        setName(name);
        setId(id);
        setExperienceYears(experienceYears);
        setBaseSalary(baseSalary);
        this.contract = new EmploymentContract("CTR-" + id, baseSalary);
    }

    public EmploymentContract getContract(){
        return contract;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            this.name = "Unknown Employee";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            this.id = 1000;
        }
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = Math.max(experienceYears, 0);
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = (baseSalary > 0) ? baseSalary : 500.0;
    }

    public abstract double calculateSalary();
    public abstract String getRoleDescription();

    public void displayInfo() {
        System.out.println("ID: " + id + " | Name: " + name +
                " | Experience: " + experienceYears + " yrs" +
                " | Base Salary: $" + baseSalary);
        System.out.println(" -> Role Info: " + getRoleDescription()) ;
        contract.displayContractDetails();
    }
}

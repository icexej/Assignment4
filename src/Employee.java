
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

public abstract class Employee implements Workable, Reportable, Comparable<Employee> {
    private String name;
    private int id;
    private int experienceYears;
    private double baseSalary;
    private EmploymentContract contract;
    private Set<MarketingProject> projects = new HashSet<>();

    public Employee() {
        this.name = "Unknown Employee";
        this.id = 1000;
        this.experienceYears = 0;
        this.baseSalary = 500.0;

        this.contract = new EmploymentContract(
                "CTR-" + this.id,
                this.baseSalary
        );
    }

    public Employee(String name, int id, int experienceYears, double baseSalary) {
        setName(name);
        setId(id);
        setExperienceYears(experienceYears);
        setBaseSalary(baseSalary);

        this.contract = new EmploymentContract(
                "CTR-" + this.id,
                this.baseSalary
        );
    }

    public EmploymentContract getContract() {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Employee)) {
            return false;
        }

        Employee other = (Employee) obj;

        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void displayInfo() {
        System.out.println("ID: " + id + " | Name: " + name +
                " | Experience: " + experienceYears + " yrs" +
                " | Base Salary: $" + baseSalary);

        System.out.println(" -> Role Info: " + getRoleDescription());

        if (contract != null) {
            contract.displayContractDetails();
        }
    }

    @Override
    public int compareTo(Employee other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Get all projects of this employee
    public Set<MarketingProject> getProjects() {
        return new HashSet<>(projects);
    }

    // Add project to employee
    public void addProject(MarketingProject project) {
        if (project != null && projects.add(project)) {
            project.addEmployeeInternal(this);
        }
    }

    // Remove project from employee
    public void removeProject(MarketingProject project) {
        if (project != null && projects.remove(project)) {
            project.removeEmployeeInternal(this);
        }
    }

    // Internal methods for synchronization
    void addProjectInternal(MarketingProject project) {
        projects.add(project);
    }

    void removeProjectInternal(MarketingProject project) {
        projects.remove(project);
    }

}

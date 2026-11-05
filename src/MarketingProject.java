
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MarketingProject {

    private int projectId;
    private String projectName;
    private double budget;

    // Employees participating in this project
    private Set<Employee> employees = new HashSet<>();

    public MarketingProject(int projectId, String projectName, double budget) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.budget = budget;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getBudget() {
        return budget;
    }

    public Set<Employee> getEmployees() {
        return new HashSet<>(employees);
    }

    // Add employee to project
    public void addEmployee(Employee employee) {
        if (employee != null && employees.add(employee)) {
            employee.addProjectInternal(this);
        }
    }

    // Remove employee from project
    public void removeEmployee(Employee employee) {
        if (employee != null && employees.remove(employee)) {
            employee.removeProjectInternal(this);
        }
    }

    // Internal methods for synchronization
    void addEmployeeInternal(Employee employee) {
        employees.add(employee);
    }

    void removeEmployeeInternal(Employee employee) {
        employees.remove(employee);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MarketingProject)) {
            return false;
        }

        MarketingProject other = (MarketingProject) obj;
        return projectId == other.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    @Override
    public String toString() {
        return "Project #" + projectId +
                " | Name: " + projectName +
                " | Budget: $" + budget;
    }
}

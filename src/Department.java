import java.util.ArrayList;
import java.util.List;

public class Department {
    private String departmentName;
    private List<Employee> employees;

    public Department(String departmentName) {
        this.departmentName = departmentName;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void showDepartmentStaff() {
        System.out.println("=== Department: " + departmentName + " ===");
        for (Employee e : employees) {
            System.out.println("   - " + e.getName() + " | Role: " + e.getRoleDescription());
        }
    }
}
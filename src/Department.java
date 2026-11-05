
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
        if (employee == null) {
            return;
        }

        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void showDepartmentStaff() {
        System.out.println("=== Department: " + departmentName + " ===");

        for (Employee e : employees) {
            System.out.println(
                    "   - " + e.getName()
                            + " | Role: " + e.getRoleDescription()
            );
        }
    }
}

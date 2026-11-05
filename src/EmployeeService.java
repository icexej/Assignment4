
import java.util.List;

public class EmployeeService {

    private EmployeeRegistry registry;

    public EmployeeService(EmployeeRegistry registry) {
        this.registry = registry;
    }

    // CREATE
    public boolean addEmployee(Employee employee) {
        return registry.addEmployee(employee);
    }

    // READ
    public Employee getEmployeeById(int id) {
        return registry.findEmployeeById(id);
    }

    public List<Employee> getAllEmployees() {
        return registry.getAllEmployees();
    }

    // UPDATE

    public boolean updateEmployee(int id, String newName,
                                  int newExperienceYears,
                                  double newBaseSalary) {

        Employee employee = registry.findEmployeeById(id);

        if (employee == null) {
            return false;
        }

        if (newName == null || newName.trim().isEmpty()
                || newExperienceYears < 0 || newBaseSalary <= 0) {
            return false;
        }

        employee.setName(newName);
        employee.setExperienceYears(newExperienceYears);
        employee.setBaseSalary(newBaseSalary);

        return true;
    }


    // DELETE
    public boolean deleteEmployee(int id) {
        return registry.removeEmployee(id);
    }

    // SEARCH
    public List<Employee> searchByName(String keyword) {

        java.util.List<Employee> result = new java.util.ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        for (Employee employee : registry.getAllEmployees()) {
            if (employee.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                result.add(employee);
            }
        }

        return result;
    }

    // ANALYTICS
    public double getAverageSalary() {

        List<Employee> employees = registry.getAllEmployees();

        if (employees.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (Employee employee : employees) {
            total += employee.calculateSalary();
        }

        return total / employees.size();
    }
}

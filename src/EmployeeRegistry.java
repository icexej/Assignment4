
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeRegistry {

    // All employees in insertion order
    private List<Employee> employees = new ArrayList<>();

    // Unique employees
    private Set<Employee> employeeSet = new HashSet<>();

    // Fast search by employee ID
    private Map<Integer, Employee> employeesById = new HashMap<>();

    // Group employees by their job title
    private Map<String, List<Employee>> employeesByRole = new HashMap<>();


    public boolean employeeIdExists(int id) {
        return employeesById.containsKey(id);
    }
    // Add employee to all collections
    public boolean addEmployee(Employee employee) {

        if (employee == null) {
            return false;
        }

        if (employeesById.containsKey(employee.getId())) {
            return false;
        }

        employees.add(employee);
        employeeSet.add(employee);
        employeesById.put(employee.getId(), employee);

        String role = employee.getClass().getSimpleName();

        employeesByRole
                .computeIfAbsent(role, key -> new ArrayList<>())
                .add(employee);

        return true;
    }

    // Find employee by ID
    public Employee findEmployeeById(int id) {
        return employeesById.get(id);
    }

    // Return all employees
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    // Return a copy of the employee set
    public Set<Employee> getEmployeeSet() {
        return new HashSet<>(employeeSet);
    }

    // Find employees by role
    public List<Employee> getEmployeesByRole(String role) {

        List<Employee> result = employeesByRole.get(role);

        if (result == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(result);
    }

    // Remove employee by ID

    public boolean removeEmployee(int id) {
        Employee employee = employeesById.remove(id);

        if (employee == null) {
            return false;
        }

        // Удаляем сотрудника из основных коллекций
        employees.remove(employee);
        employeeSet.remove(employee);

        // Удаляем из группы по должности
        String role = employee.getClass().getSimpleName();
        List<Employee> group = employeesByRole.get(role);

        if (group != null) {
            group.remove(employee);

            if (group.isEmpty()) {
                employeesByRole.remove(role);
            }
        }

        // Удаляем сотрудника из всех его проектов
        for (MarketingProject project : employee.getProjects()) {
            employee.removeProject(project);
        }

        return true;
    }


    // Get number of employees
    public int getEmployeeCount() {
        return employees.size();
    }
}

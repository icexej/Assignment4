
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    private static String readName(Scanner scanner) {
        String name;

        do {
            System.out.print("Enter employee name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
            }

        } while (name.isEmpty());

        return name;
    }

    private static int readPositiveInt(Scanner scanner, String message) {
        int value;

        do {
            System.out.print(message);
            value = scanner.nextInt();

            if (value <= 0) {
                System.out.println("Enter a number greater than 0!");
            }

        } while (value <= 0);

        return value;
    }


    private static int readNonNegativeInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int value = scanner.nextInt();
            scanner.nextLine();

            if (value < 0) {
                System.out.println("Value cannot be negative!");
                continue;
            }

            return value;
        }
    }


    private static double readNonNegativeDouble(Scanner scanner, String message) {
        double value;

        do {
            System.out.print(message);
            value = scanner.nextDouble();

            if (!Double.isFinite(value) || value < 0) {
                System.out.println("Enter a valid non-negative number!");
            }

        } while (!Double.isFinite(value) || value < 0);

        return value;
    }

    private static double readPercentage(Scanner scanner, String message) {
        double value;

        do {
            System.out.print(message);
            value = scanner.nextDouble();

            if (!Double.isFinite(value) || value < 0 || value > 100) {
                System.out.println("Percentage must be between 0 and 100!");
            }

        } while (!Double.isFinite(value) || value < 0 || value > 100);

        return value;
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AuthService authService = new AuthService();
        EmployeeRegistry registry = new EmployeeRegistry();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        EmployeeService employeeService = new EmployeeService(registry);
        ProjectService projectService = new ProjectService();
        RequestQueue requestQueue = new RequestQueue();
        ProjectDAO projectDAO = new ProjectDAO();
        RequestDAO requestDAO = new RequestDAO();

        for (MarketingProject savedProject : projectDAO.getAllProjects()) {
            projectService.addProject(savedProject);
        }

        System.out.println("Loaded projects: " + projectService.getProjectCount());
        int nextRequestId = requestDAO.getNextRequestId();

        for (Employee savedEmployee : employeeDAO.getAllEmployees()) {
            registry.addEmployee(savedEmployee);
        }

        projectDAO.loadAssignments(projectService, employeeService);
        requestDAO.loadPendingRequests(requestQueue, employeeService);

        System.out.println("Loaded employees: " + registry.getEmployeeCount());

        boolean running = true;

        while (running) {

            System.out.println("\n===== MARKETING MANAGEMENT SYSTEM =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Employee Login");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Login: ");
                    String login = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    if (authService.loginAdmin(login, password)) {

                        System.out.println("\nWelcome, Admin!");

                        boolean adminMenu = true;

                        while (adminMenu) {

                            System.out.println("1. Add employee");
                            System.out.println("2. View all employees");
                            System.out.println("3. Search employee");
                            System.out.println("4. Update employee");
                            System.out.println("5. Delete employee");
                            System.out.println("6. Create project");
                            System.out.println("7. Assign employee to project");
                            System.out.println("8. View all projects");
                            System.out.println("9. Delete project");
                            System.out.println("10. Process employee requests");
                            System.out.println("11. View payroll report");
                            System.out.println("12. Sort employees by salary");
                            System.out.println("13. View employee statistics");
                            System.out.println("14. Export employees to CSV");
                            System.out.println("0. Logout");
                            System.out.print("Choose an option: ");

                            int adminChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (adminChoice) {

                                case 1:
                                    System.out.println("\nChoose employee role:");
                                    System.out.println("1. Social Media Manager");
                                    System.out.println("2. Content Creator");
                                    System.out.println("3. Targetologist");
                                    System.out.print("Role: ");

                                    int role = scanner.nextInt();
                                    scanner.nextLine();

                                    String name = readName(scanner);

                                    int id;

                                    while (true) {
                                        id = readPositiveInt(scanner, "Enter employee ID: ");

                                        if (registry.employeeIdExists(id)) {
                                            System.out.println("This ID already exists! Please enter another ID.");
                                        } else {
                                            break;
                                        }
                                    }

                                    int experience = readNonNegativeInt(
                                            scanner, "Enter work experience (years): "
                                    );

                                    double salary = readNonNegativeDouble(
                                            scanner, "Enter monthly base salary (KGS): "
                                    );
                                    scanner.nextLine();

                                    Employee employee = null;

                                    if (role == 1) {
                                        System.out.print("Platform: ");
                                        String platform = scanner.nextLine();

                                        System.out.print("Followers count: ");
                                        int followers = scanner.nextInt();
                                        scanner.nextLine();

                                        employee = new SocialMediaManager(
                                                name, id, experience, salary,
                                                platform, followers
                                        );

                                    } else if (role == 2) {
                                        System.out.print("Content type: ");
                                        String contentType = scanner.nextLine();

                                        System.out.print("Videos per week: ");
                                        int videos = scanner.nextInt();
                                        scanner.nextLine();

                                        employee = new ContentCreator(
                                                name, id, experience, salary,
                                                contentType, videos
                                        );

                                    } else if (role == 3) {
                                        double budget = readNonNegativeDouble(
                                                scanner, "Enter monthly advertising budget (KGS): "
                                        );

                                        double rate = readPercentage(
                                                scanner, "Enter conversion rate (%): "
                                        );

                                        scanner.nextLine();

                                        employee = new Targetologist(
                                                name, id, experience, salary,
                                                budget, rate
                                        );

                                    } else {
                                        System.out.println("Invalid role.");
                                    }

                                    if (employeeService.addEmployee(employee)) {

                                        System.out.println("Employee added successfully!");

                                        String employeePassword;

                                        do {
                                            System.out.print("Create employee password (minimum 4 characters): ");
                                            employeePassword = scanner.nextLine();

                                            if (employeePassword.length() < 4) {
                                                System.out.println("Password must contain at least 4 characters!");
                                            }

                                        } while (employeePassword.length() < 4);

                                        employee.setPassword(employeePassword);

                                        employeeDAO.saveEmployee(employee);

                                        System.out.println("Employee account created successfully!");

                                    } else {
                                        System.out.println("Employee with this ID already exists.");
                                    }
                                    break;

                                case 2: {
                                    System.out.println("\n===== EMPLOYEE LIST =====");

                                    List<Employee> employees = employeeService.getAllEmployees();

                                    if (employees.isEmpty()) {
                                        System.out.println("No employees found.");
                                        break;
                                    }

                                    System.out.println("1. Sort by salary");
                                    System.out.println("2. Sort by experience");
                                    System.out.println("3. Sort by name");
                                    System.out.println("4. Sort Social Media Managers by followers");
                                    System.out.println("5. Show without sorting");

                                    int sortChoice = readPositiveInt(scanner, "Choose sorting option: ");

                                    switch (sortChoice) {
                                        case 1:
                                            employees.sort(EmployeeComparators.bySalary);
                                            break;
                                        case 2:
                                            employees.sort(EmployeeComparators.byExperience);
                                            break;
                                        case 3:
                                            employees.sort(null);
                                            break;
                                        case 4:
                                            employees.removeIf(emp -> !(emp instanceof SocialMediaManager));
                                            employees.sort((a, b) ->
                                                    EmployeeComparators.byFollowers.compare(
                                                            (SocialMediaManager) a,
                                                            (SocialMediaManager) b
                                                    )
                                            );
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Invalid choice. Showing without sorting.");
                                    }

                                    for (Employee emp : employees) {
                                        emp.displayInfo();
                                        System.out.println("--------------------");
                                    }

                                    break;
                                }

                                case 3:
                                    System.out.print("Enter employee name: ");
                                    String searchName = scanner.nextLine();

                                    var results = employeeService.searchByName(searchName);

                                    if (results.isEmpty()) {
                                        System.out.println("Employee not found.");
                                    } else {
                                        System.out.println("\n===== SEARCH RESULTS =====");
                                        for (Employee emp : results) {
                                            emp.displayInfo();
                                            System.out.println("--------------------");
                                        }
                                    }
                                    break;

                                case 4:
                                    System.out.print("Enter employee ID: ");
                                    int updateId = scanner.nextInt();
                                    scanner.nextLine();

                                    Employee employeeToUpdate = employeeService.getEmployeeById(updateId);

                                    if (employeeToUpdate == null) {
                                        System.out.println("Employee not found.");
                                    } else {
                                        System.out.print("New name: ");
                                        String newName = scanner.nextLine();

                                        System.out.print("New experience (years): ");
                                        int newExperience = scanner.nextInt();

                                        System.out.print("New base salary: ");
                                        double newSalary = scanner.nextDouble();
                                        scanner.nextLine();

                                        employeeService.updateEmployee(
                                                updateId, newName, newExperience, newSalary
                                        );

                                        Employee updatedEmployee = employeeService.getEmployeeById(updateId);

                                        employeeDAO.saveEmployee(updatedEmployee);

                                        System.out.println("Employee updated successfully!");
                                    }
                                    break;

                                case 5:
                                    System.out.print("Enter employee ID to delete: ");
                                    int deleteId = scanner.nextInt();
                                    scanner.nextLine();

                                    Employee employeeToDelete = employeeService.getEmployeeById(deleteId);

                                    if (employeeToDelete == null) {
                                        System.out.println("Employee not found.");
                                    } else {
                                        employeeDAO.deleteEmployee(deleteId);
                                        employeeService.deleteEmployee(deleteId);
                                        System.out.println("Employee deleted successfully!");
                                    }
                                    break;
                                case 6: {
                                    System.out.println("\n===== CREATE PROJECT =====");

                                    int projectId;

                                    while (true) {
                                        projectId = readPositiveInt(scanner, "Enter project ID: ");

                                        if (projectService.getProjectById(projectId) != null) {
                                            System.out.println("This project ID already exists! Enter another ID.");
                                        } else {
                                            break;
                                        }
                                    }

                                    String projectName = readName(scanner);

                                    double budget = readNonNegativeDouble(scanner, "Enter project budget: ");

                                    MarketingProject project = new MarketingProject(
                                            projectId,
                                            projectName,
                                            budget
                                    );

                                    if (projectService.addProject(project)) {

                                        projectDAO.saveProject(project);

                                        System.out.println("Project created successfully!");

                                    } else {
                                        System.out.println("Could not create project.");
                                    }

                                    break;
                                }
                                case 7: {
                                    System.out.println("\n===== ASSIGN EMPLOYEE TO PROJECT =====");

                                    int projectId = readPositiveInt(scanner, "Enter project ID: ");

                                    MarketingProject project = projectService.getProjectById(projectId);

                                    if (project == null) {
                                        System.out.println("Project not found!");
                                        break;
                                    }

                                    int employeeId = readPositiveInt(scanner, "Enter employee ID: ");

                                    Employee assignedEmployee = employeeService.getEmployeeById(employeeId);
                                    if (assignedEmployee == null) {
                                        System.out.println("Employee not found!");
                                        break;
                                    }

                                    if (project.getEmployees().contains(assignedEmployee)) {
                                        System.out.println("This employee is already assigned to this project!");
                                        break;
                                    }

                                    projectService.assignEmployee(projectId, assignedEmployee);
                                    projectDAO.assignEmployee(employeeId, projectId);

                                    System.out.println(assignedEmployee.getName() +
                                            " successfully assigned to " + project.getProjectName());

                                    break;
                                }
                                case 8: {
                                    System.out.println("\n===== ALL MARKETING PROJECTS =====");

                                    if (projectService.getAllProjects().isEmpty()) {
                                        System.out.println("No projects found.");
                                        break;
                                    }

                                    for (MarketingProject project : projectService.getAllProjects()) {
                                        System.out.println("\n" + project);
                                        System.out.println("Assigned employees:");

                                        if (project.getEmployees().isEmpty()) {
                                            System.out.println("No employees assigned yet.");
                                        } else {
                                            for (Employee assignedEmployee : project.getEmployees()) {
                                                System.out.println(
                                                        "- " + assignedEmployee.getName() +
                                                                " (ID: " + assignedEmployee.getId() + ")"
                                                );
                                            }
                                        }
                                    }

                                    break;
                                }
                                case 9: {
                                    System.out.println("\n===== DELETE PROJECT =====");

                                    int projectId = readPositiveInt(scanner, "Enter project ID: ");

                                    MarketingProject project = projectService.getProjectById(projectId);

                                    if (project == null) {
                                        System.out.println("Project not found!");
                                        break;
                                    }

                                    System.out.println("Project: " + project.getProjectName());
                                    System.out.println("Assigned employees: " + project.getEmployees().size());

                                    System.out.print("Are you sure you want to delete this project? (yes/no): ");
                                    String confirmation = scanner.next();

                                    scanner.nextLine();
                                    if (confirmation.equalsIgnoreCase("yes")) {
                                        if (projectService.deleteProject(projectId)) {

                                            projectDAO.deleteProject(projectId);

                                            System.out.println("Project deleted successfully!");

                                        } else {
                                            System.out.println("Could not delete project.");
                                        }
                                    } else {
                                        System.out.println("Deletion cancelled.");
                                    }

                                    break;
                                }
                                case 10: {
                                    System.out.println("\n===== PROCESS EMPLOYEE REQUESTS =====");

                                    System.out.println("Pending requests: " + requestQueue.getRequestCount());

                                    if (requestQueue.getRequestCount() == 0) {
                                        System.out.println("No requests available.");
                                        break;
                                    }

                                    System.out.println("1. Process next request (FIFO)");
                                    System.out.println("2. Process highest priority request");

                                    int processChoice = readPositiveInt(scanner, "Choose option: ");

                                    if (processChoice != 1 && processChoice != 2) {
                                        System.out.println("Invalid option!");
                                        break;
                                    }

                                    int executorId = readPositiveInt(scanner, "Enter executor employee ID: ");
                                    Employee executor = employeeService.getEmployeeById(executorId);

                                    if (executor == null) {
                                        System.out.println("Employee not found!");
                                        break;
                                    }

                                    EmployeeRequest processedRequest;

                                    if (processChoice == 1) {
                                        processedRequest = requestQueue.processNextRequest(executor);
                                    } else {
                                        processedRequest = requestQueue.processHighestPriorityRequest(executor);
                                    }

                                    if (processedRequest != null) {
                                        requestDAO.updateRequest(processedRequest);
                                        System.out.println("Request successfully processed!");
                                    }

                                    break;
                                }
                                case 11: {
                                    System.out.println("\n===== PAYROLL REPORT =====");

                                    List<Employee> employees = employeeService.getAllEmployees();

                                    if (employees.isEmpty()) {
                                        System.out.println("No employees found.");
                                        break;
                                    }

                                    double totalPayroll = 0;


                                    for (Employee emp : employees) {
                                        double employeeSalary = emp.calculateSalary();

                                        System.out.println("\nEmployee: " + emp.getName());
                                        System.out.println("ID: " + emp.getId());
                                        System.out.println("Role: " + emp.getClass().getSimpleName());
                                        System.out.println("Salary: " + employeeSalary + " KGS");

                                        totalPayroll += employeeSalary;
                                    }


                                    System.out.println("\n----------------------------");
                                    System.out.println("Total employees: " + employees.size());
                                    System.out.println("Total monthly payroll: " + totalPayroll + " KGS");

                                    break;
                                }
                                case 12: {
                                    System.out.println("\n===== EMPLOYEES SORTED BY SALARY =====");

                                    List<Employee> sortedEmployees =
                                            new ArrayList<>(employeeService.getAllEmployees());

                                    if (sortedEmployees.isEmpty()) {
                                        System.out.println("No employees found.");
                                        break;
                                    }

                                    sortedEmployees.sort(
                                            (e1, e2) -> Double.compare(
                                                    e2.calculateSalary(),
                                                    e1.calculateSalary()
                                            )
                                    );

                                    for (Employee emp : sortedEmployees) {
                                        System.out.println(
                                                emp.getName() +
                                                        " | ID: " + emp.getId() +
                                                        " | Salary: " + emp.calculateSalary() + " KGS"
                                        );
                                    }

                                    break;
                                }

                                case 13: {
                                    System.out.println("\n===== EMPLOYEE STATISTICS =====");

                                    List<Employee> employees = employeeService.getAllEmployees();

                                    if (employees.isEmpty()) {
                                        System.out.println("No employees found.");
                                        break;
                                    }

                                    Map<String, Integer> counts = new HashMap<>();
                                    Map<String, Double> salaries = new HashMap<>();

                                    for (Employee emp : employees) {
                                        String employeeRole = emp.getClass().getSimpleName();

                                        counts.put(employeeRole,
                                                counts.getOrDefault(employeeRole, 0) + 1);

                                        salaries.put(
                                                employeeRole,
                                                salaries.getOrDefault(employeeRole, 0.0)
                                                        + emp.calculateSalary()
                                        );
                                    }

                                    for (String employeeRole : counts.keySet()) {
                                        int count = counts.get(employeeRole);
                                        double averageSalary = salaries.get(employeeRole) / count;

                                        System.out.println("\nRole: " + employeeRole);
                                        System.out.println("Employees: " + count);
                                        System.out.println("Average salary: " + averageSalary + " KGS");
                                    }

                                    break;
                                }

                                case 14: {
                                    System.out.println("\n===== EXPORT EMPLOYEES =====");

                                    List<Employee> employees = employeeService.getAllEmployees();

                                    if (employees.isEmpty()) {
                                        System.out.println("No employees to export.");
                                        break;
                                    }

                                    List<String> csvLines = new ArrayList<>();
                                    csvLines.add("ID,Name,Role,Salary");

                                    for (Employee emp : employees) {
                                        double employeeSalary = emp.calculateSalary();

                                        String line =
                                                emp.getId() + "," +
                                                        escapeCsv(emp.getName()) + "," +
                                                        emp.getClass().getSimpleName() + "," +
                                                        employeeSalary;

                                        csvLines.add(line);
                                    }

                                    try {
                                        Files.write(
                                                Path.of("employees.csv"),
                                                csvLines,
                                                StandardCharsets.UTF_8
                                        );

                                        System.out.println("Employees exported successfully!");
                                        System.out.println("File: employees.csv");

                                    } catch (IOException e) {
                                        System.out.println("Error exporting employees: " + e.getMessage());
                                    }

                                    break;
                                }


                                case 0:
                                    adminMenu = false;
                                    System.out.println("Logged out.");
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }
                        }

                    } else {
                        System.out.println("Wrong login or password!");
                    }
                    break;

                case 2:
                    System.out.print("Enter your employee ID: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter your password: ");
                    String employeePassword = scanner.nextLine();

                    Employee employee = employeeService.getEmployeeById(employeeId);

                    if (authService.loginEmployee(employee, employeePassword)) {
                        Employee loggedInEmployee = employee;

                        if (loggedInEmployee == null) {
                            System.out.println("Employee account not found.");
                            break;
                        }

                        System.out.println("\nWelcome, " + loggedInEmployee.getName() + "!");

                        boolean employeeMenu = true;

                        while (employeeMenu) {

                            System.out.println("\n===== EMPLOYEE MENU =====");
                            System.out.println("1. View my profile");
                            System.out.println("2. View my salary");
                            System.out.println("3. View my projects");
                            System.out.println("4. Submit a request");
                            System.out.println("0. Logout");
                            System.out.print("Choose an option: ");

                            int employeeChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (employeeChoice) {

                                case 1:
                                    loggedInEmployee.displayInfo();
                                    break;

                                case 2:
                                    System.out.println(
                                            "Your calculated salary: "
                                                    + loggedInEmployee.calculateSalary() + " KGS"
                                    );
                                    break;
                                case 3:
                                    System.out.println("\n===== MY PROJECTS =====");

                                    if (loggedInEmployee.getProjects().isEmpty()) {
                                        System.out.println("You are not assigned to any projects yet.");
                                    } else {
                                        for (MarketingProject project : loggedInEmployee.getProjects()) {
                                            System.out.println("\nProject ID: " + project.getProjectId());
                                            System.out.println("Project name: " + project.getProjectName());
                                            System.out.println("Budget: " + project.getBudget() + " KGS");
                                        }
                                    }
                                    break;
                                case 4: {
                                    System.out.println("\n===== SUBMIT A REQUEST =====");

                                    System.out.print("Describe your request: ");
                                    String description = scanner.nextLine();

                                    while (description.trim().isEmpty()) {
                                        System.out.println("Request cannot be empty!");
                                        System.out.print("Describe your request: ");
                                        description = scanner.nextLine();
                                    }

                                    System.out.print("Enter priority (high, medium, low): ");
                                    String priority = scanner.nextLine();

                                    while (!priority.equalsIgnoreCase("high")
                                            && !priority.equalsIgnoreCase("medium")
                                            && !priority.equalsIgnoreCase("low")) {

                                        System.out.println("Please enter high, medium or low.");
                                        System.out.print("Enter priority: ");
                                        priority = scanner.nextLine();
                                    }

                                    EmployeeRequest request = new EmployeeRequest(
                                            nextRequestId,
                                            description,
                                            priority,
                                            loggedInEmployee
                                    );

                                    requestQueue.submitRequest(request);
                                    requestDAO.saveRequest(request);

                                    System.out.println("Your request has been submitted successfully!");
                                    nextRequestId++;

                                    break;
                                }

                                case 0:
                                    employeeMenu = false;
                                    System.out.println("Logged out.");
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }
                        }

                    } else {
                        System.out.println("Invalid employee ID or password!");
                    }
                    break;


                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}

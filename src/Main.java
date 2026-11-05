public class Main {
    public static void main(String[] args) {
        SocialMediaManager smm = new SocialMediaManager("Aizhan", 101, 2, 900.0, "Instagram", 30000);
        ContentCreator creator = new ContentCreator("Timur", 102, 1, 800.0, "Reels & TikTok", 15);

        System.out.println("=== 1. INHERITANCE, POLYMORPHISM & COMPOSITION ===");
        smm.displayInfo(); // Внутри вызывается контракт (Композиция)

        System.out.println("\n=== 2. AGGREGATION DEMO ===");
        Department marketing = new Department("Marketing Department");
        marketing.addEmployee(smm);
        marketing.addEmployee(creator);
        marketing.showDepartmentStaff();

        System.out.println("\n=== 3. DEPENDENCY DEMO ===");
        PayrollSystem payroll = new PayrollSystem();
        payroll.processPayment(smm); // PayrollSystem использует Employee


        System.out.println("\n=== 4. EMPLOYEE REGISTRY DEMO ===");

        EmployeeRegistry registry = new EmployeeRegistry();

// Add employees
        registry.addEmployee(smm);
        registry.addEmployee(creator);

        System.out.println("Total employees: " + registry.getEmployeeCount());

// Search by ID
        Employee found = registry.findEmployeeById(101);

        if (found != null) {
            System.out.println("Found employee: " + found.getName());
        }

// Check Set
        System.out.println("Employees in Set: " + registry.getEmployeeSet().size());

// Group by role
        System.out.println("Content Creators: " +
                registry.getEmployeesByRole("ContentCreator").size());

// Try to add duplicate ID
        SocialMediaManager duplicate =
                new SocialMediaManager("Another Person", 101, 5, 1200.0, "TikTok", 50000);

        System.out.println("Duplicate added: " + registry.addEmployee(duplicate));

        System.out.println("Total employees after duplicate attempt: " +
                registry.getEmployeeCount());


        System.out.println("\n=== 5. EMPLOYEE SORTING DEMO ===");

        java.util.List<Employee> sortedEmployees = registry.getAllEmployees();

// 1. Natural order: by name
        System.out.println("\nSorted by name:");
        sortedEmployees.sort(null);

        for (Employee employee : sortedEmployees) {
            System.out.println(employee.getName());
        }

// 2. By salary
        System.out.println("\nSorted by salary:");
        sortedEmployees.sort(EmployeeComparators.bySalary);

        for (Employee employee : sortedEmployees) {
            System.out.println(employee.getName() + " - $" +
                    employee.calculateSalary());
        }

// 3. By experience
        System.out.println("\nSorted by experience:");
        sortedEmployees.sort(EmployeeComparators.byExperience);

        for (Employee employee : sortedEmployees) {
            System.out.println(employee.getName() + " - " +
                    employee.getExperienceYears() + " years");
        }

// 4. By followers
        System.out.println("\nSorted by followers:");

        java.util.List<SocialMediaManager> managers =
                new java.util.ArrayList<>();

        for (Employee employee : registry.getAllEmployees()) {
            if (employee instanceof SocialMediaManager) {
                managers.add((SocialMediaManager) employee);
            }
        }

        managers.sort(EmployeeComparators.byFollowers);

        for (SocialMediaManager manager : managers) {
            System.out.println(manager.getName() + " - " +
                    manager.getFollowersCount() + " followers");
        }



        System.out.println("\n=== 6. REQUEST QUEUE DEMO ===");

        RequestQueue requestQueue = new RequestQueue();

        EmployeeRequest request1 = new EmployeeRequest(
                1, "Create Instagram post", "Low", smm
        );

        EmployeeRequest request2 = new EmployeeRequest(
                2, "Fix urgent advertising campaign", "High", creator
        );

        EmployeeRequest request3 = new EmployeeRequest(
                3, "Prepare TikTok video", "Medium", smm
        );

        requestQueue.submitRequest(request1);
        requestQueue.submitRequest(request2);
        requestQueue.submitRequest(request3);

        System.out.println("Total requests: "
                + requestQueue.getRequestCount());

// FIFO processing
        requestQueue.processNextRequest(creator);

        System.out.println("Remaining requests: "
                + requestQueue.getRequestCount());

// Priority processing
        requestQueue.processHighestPriorityRequest(smm);

        System.out.println("Remaining requests: "
                + requestQueue.getRequestCount());




        System.out.println("\n=== 7. EMPLOYEE SERVICE DEMO ===");

        EmployeeService employeeService = new EmployeeService(registry);

// READ
        System.out.println("Employee with ID 101: " +
                employeeService.getEmployeeById(101).getName());

// UPDATE
        boolean updated = employeeService.updateEmployee(
                101,
                "Aizhan Updated",
                3,
                1200
        );

        System.out.println("Employee updated: " + updated);

        Employee updatedEmployee = employeeService.getEmployeeById(101);

        if (updatedEmployee != null) {
            System.out.println("Updated experience: " + updatedEmployee.getExperienceYears());
            System.out.println("Updated salary: $" + updatedEmployee.getBaseSalary());
        }
        System.out.println("Updated name: " +
                employeeService.getEmployeeById(101).getName());

// SEARCH
        System.out.println("Search result for 'Timur': " +
                employeeService.searchByName("Timur").size());

// ANALYTICS
        System.out.println("Average salary: $" +
                employeeService.getAverageSalary());

// DELETE TEST
        System.out.println("Employee deleted: " +
                employeeService.deleteEmployee(101));

        System.out.println("Remaining employees: " +
                employeeService.getAllEmployees().size());



        System.out.println("\n=== 8. MANY-TO-MANY RELATIONSHIP DEMO ===");

        MarketingProject project1 =
                new MarketingProject(1, "Instagram Campaign", 5000);

        MarketingProject project2 =
                new MarketingProject(2, "TikTok Campaign", 3000);

// Add employees to projects
        project1.addEmployee(smm);
        project1.addEmployee(creator);

        project2.addEmployee(smm);

// Show projects of Aizhan
        System.out.println("\nProjects of Aizhan:");

        for (MarketingProject project : smm.getProjects()) {
            System.out.println(project.getProjectName());
        }

// Show employees in Instagram Campaign
        System.out.println("\nEmployees in Instagram Campaign:");

        for (Employee employee : project1.getEmployees()) {
            System.out.println(employee.getName());
        }

// Check relationship synchronization
        System.out.println("\nAizhan's project count: " +
                smm.getProjects().size());

        System.out.println("Instagram Campaign employee count: " +
                project1.getEmployees().size());



        System.out.println("\n=== 9. PROJECT SERVICE DEMO ===");

        ProjectService projectService = new ProjectService();

        projectService.addProject(project1);
        projectService.addProject(project2);

        System.out.println("Total projects: " + projectService.getProjectCount());

        MarketingProject foundProject = projectService.getProjectById(1);

        if (foundProject != null) {
            System.out.println("Found project: " + foundProject.getProjectName());
        }

        System.out.println("All projects:");

        for (MarketingProject project : projectService.getAllProjects()) {
            System.out.println(project);
        }

        System.out.println(
                "Duplicate project added: " +
                        projectService.addProject(
                                new MarketingProject(1, "Another Campaign", 1000)
                        )
        );

        System.out.println("\n=== 10. SYNCHRONIZED DELETION TEST ===");

        System.out.println(
                "Employees before deletion: " +
                        project1.getEmployees().size()
        );

        Employee employeeToDelete = project1.getEmployees()
                .stream()
                .filter(employee -> employee.getId() == 102)                .findFirst()
                .orElse(null);

        if (employeeToDelete != null) {
            registry.removeEmployee(employeeToDelete.getId());
        }

        System.out.println(
                "Employees after deletion: " +
                        project1.getEmployees().size()
        );

        System.out.println(
                "Projects of deleted employee: " +
                        (employeeToDelete == null ? 0 : employeeToDelete.getProjects().size())
        );


        System.out.println("\n=== DEPARTMENT RELATIONSHIP TEST ===");

        System.out.println("Department: " + marketing.getDepartmentName());

        System.out.println("Number of employees: "
                + marketing.getEmployees().size());

        marketing.removeEmployee(creator);

        System.out.println("After removing employee: "
                + marketing.getEmployees().size());

    }
}



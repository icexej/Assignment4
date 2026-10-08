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
    }
}



public class PayrollSystem {

    public void processPayment(Employee employee) {
        double total = employee.calculateSalary();
        System.out.println("[PayrollSystem] Successfully processed payment of $" + total + " for " + employee.getName());
    }
}
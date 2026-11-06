public class AuthService {

    private final String adminLogin = "admin";
    private final String adminPassword = "admin123";

    // Admin authentication
    public boolean loginAdmin(String login, String password) {
        return adminLogin.equals(login)
                && adminPassword.equals(password);
    }

    // Employee authentication
    public boolean loginEmployee(Employee employee, String password) {
        return employee != null
                && employee.getPassword() != null
                && employee.getPassword().equals(password);
    }
}
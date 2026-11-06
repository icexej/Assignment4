import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void saveEmployee(Employee employee) {

        String sql = """
    INSERT OR REPLACE INTO employees
    (id, name, role, experience_years, base_salary, password,
     platform, followers_count, content_type,
     weekly_videos, monthly_ad_budget, conversion_rate)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getClass().getSimpleName());
            statement.setInt(4, employee.getExperienceYears());
            statement.setDouble(5, employee.getBaseSalary());
            statement.setString(6, employee.getPassword());

            statement.setString(7, null);
            statement.setInt(8, 0);
            statement.setString(9, null);
            statement.setInt(10, 0);
            statement.setDouble(11, 0);
            statement.setDouble(12, 0);

            if (employee instanceof SocialMediaManager smm) {
                statement.setString(7, smm.getPlatform());
                statement.setInt(8, smm.getFollowersCount());

            } else if (employee instanceof ContentCreator creator) {
                statement.setString(9, creator.getContentType());
                statement.setInt(10, creator.getWeeklyVideos());

            } else if (employee instanceof Targetologist targetologist) {
                statement.setDouble(11, targetologist.getMonthlyAdBudget());
                statement.setDouble(12, targetologist.getConversionRate());
            }

            statement.executeUpdate();

            System.out.println("Employee saved to database!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        String sql = """
            SELECT * FROM employees
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                int id = result.getInt("id");
                String name = result.getString("name");
                String role = result.getString("role");
                int experience = result.getInt("experience_years");
                double salary = result.getDouble("base_salary");

                Employee employee = null;

                if (role.equals("SocialMediaManager")) {

                    employee = new SocialMediaManager(
                            name, id, experience, salary,
                            result.getString("platform"),
                            result.getInt("followers_count")
                    );

                } else if (role.equals("ContentCreator")) {

                    employee = new ContentCreator(
                            name, id, experience, salary,
                            result.getString("content_type"),
                            result.getInt("weekly_videos")
                    );

                } else if (role.equals("Targetologist")) {

                    employee = new Targetologist(
                            name, id, experience, salary,
                            result.getDouble("monthly_ad_budget"),
                            result.getDouble("conversion_rate")
                    );
                }

                if (employee != null) {
                    employee.setPassword(result.getString("password"));
                    employees.add(employee);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return employees;
    }
    public void deleteEmployee(int id) {
        String deleteAssignments =
                "DELETE FROM employee_projects WHERE employee_id = ?";
        String deleteEmployee =
                "DELETE FROM employees WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement assignmentStatement =
                         connection.prepareStatement(deleteAssignments);
                 PreparedStatement employeeStatement =
                         connection.prepareStatement(deleteEmployee)) {

                assignmentStatement.setInt(1, id);
                assignmentStatement.executeUpdate();

                employeeStatement.setInt(1, id);
                employeeStatement.executeUpdate();

                connection.commit();

                System.out.println("Employee deleted from database!");

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // CREATE: save project
    public void saveProject(MarketingProject project) {

        String sql = """
                INSERT OR REPLACE INTO projects
                (project_id, project_name, budget)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, project.getProjectId());
            statement.setString(2, project.getProjectName());
            statement.setDouble(3, project.getBudget());

            statement.executeUpdate();

            System.out.println("Project saved to database!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // READ: load all projects
    public List<MarketingProject> getAllProjects() {

        List<MarketingProject> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                int id = result.getInt("project_id");
                String name = result.getString("project_name");
                double budget = result.getDouble("budget");

                MarketingProject project =
                        new MarketingProject(id, name, budget);

                projects.add(project);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return projects;
    }

    // DELETE: remove project
    public void deleteProject(int id) {

        String deleteAssignments =
                "DELETE FROM employee_projects WHERE project_id = ?";

        String deleteProject =
                "DELETE FROM projects WHERE project_id = ?";

        try (Connection connection = DatabaseManager.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement assignmentStatement =
                         connection.prepareStatement(deleteAssignments);
                 PreparedStatement projectStatement =
                         connection.prepareStatement(deleteProject)) {

                assignmentStatement.setInt(1, id);
                assignmentStatement.executeUpdate();

                projectStatement.setInt(1, id);
                projectStatement.executeUpdate();

                connection.commit();

                System.out.println("Project deleted from database!");

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void assignEmployee(int employeeId, int projectId) {
        String sql = """
            INSERT OR IGNORE INTO employee_projects (employee_id, project_id)
            VALUES (?, ?)
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            statement.setInt(2, projectId);
            statement.executeUpdate();

            System.out.println("Assignment saved to database!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void loadAssignments(ProjectService projectService,
                                EmployeeService employeeService) {

        String sql = "SELECT employee_id, project_id FROM employee_projects";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                int projectId = resultSet.getInt("project_id");

                Employee employee = employeeService.getEmployeeById(employeeId);

                if (employee != null &&
                        projectService.getProjectById(projectId) != null) {

                    projectService.assignEmployee(projectId, employee);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
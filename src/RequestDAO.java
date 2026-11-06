import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestDAO {

    public void saveRequest(EmployeeRequest request) {

        String sql = """
                INSERT OR REPLACE INTO requests
                (request_id, description, priority, requester_id, executor_id, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, request.getRequestId());
            statement.setString(2, request.getDescription());
            statement.setString(3, request.getPriority());
            statement.setInt(4, request.getRequester().getId());

            if (request.getExecutor() == null) {
                statement.setNull(5, java.sql.Types.INTEGER);
            } else {
                statement.setInt(5, request.getExecutor().getId());
            }

            statement.setString(6, "PENDING");

            statement.executeUpdate();

            System.out.println("Request saved to database!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void updateRequest(EmployeeRequest request) {

        String sql = """
            UPDATE requests
            SET executor_id = ?, status = ?
            WHERE request_id = ?
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (request.getExecutor() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, request.getExecutor().getId());
            }

            statement.setString(2, "PROCESSED");
            statement.setInt(3, request.getRequestId());

            statement.executeUpdate();

            System.out.println("Request updated in database!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void loadPendingRequests(RequestQueue requestQueue,
                                    EmployeeService employeeService) {

        String sql = """
            SELECT request_id, description, priority, requester_id, executor_id
            FROM requests
            WHERE status = 'PENDING'
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int requestId = resultSet.getInt("request_id");
                String description = resultSet.getString("description");
                String priority = resultSet.getString("priority");
                int requesterId = resultSet.getInt("requester_id");

                Employee requester =
                        employeeService.getEmployeeById(requesterId);

                if (requester != null) {
                    EmployeeRequest request = new EmployeeRequest(
                            requestId,
                            description,
                            priority,
                            requester
                    );

                    requestQueue.submitRequest(request);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public int getNextRequestId() {

        String sql = "SELECT COALESCE(MAX(request_id), 0) + 1 FROM requests";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            return resultSet.getInt(1);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return 1;
        }
    }
}
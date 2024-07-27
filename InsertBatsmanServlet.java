import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/insert")
public class InsertBatsmanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/praveen";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Praveen@123";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String role = request.getParameter("role");
        int totalRuns = Integer.parseInt(request.getParameter("total_runs"));
        double income = Double.parseDouble(request.getParameter("income"));
        String nativePlace = request.getParameter("native_place");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Sucessfull");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO batsmen (name, role, total_runs, income, native_place) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, role);
            statement.setInt(3, totalRuns);
            statement.setDouble(4, income);
            statement.setString(5, nativePlace);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("insertion-successfull.html");
            } else {
                response.sendRedirect("add-batsman.html?error=true");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

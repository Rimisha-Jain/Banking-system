import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String user = "root";
        String password = "MySQL09@@";
        return DriverManager.getConnection(url, user, password);
    }
}


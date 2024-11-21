package tp5;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    private DatabaseConnection connection;

    public CreateTable(DatabaseConnection conn) {
        this.connection = conn;
    }


    public void createStudentTable() throws SQLException {
        try (Connection conn = connection.getConnection();
             Statement statement = conn.createStatement()) {

            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS Student (
                        id INT AUTO_INCREMENT PRIMARY KEY, 
                        name VARCHAR(100) NOT NULL, 
                        age INT NOT NULL) 
                    """;
            statement.execute(createTableSQL);
        }
    }
}

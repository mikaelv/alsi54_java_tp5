package tp5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PopulateTable {
    private DatabaseConnection conn;

    public PopulateTable(DatabaseConnection conn) {
        this.conn = conn;
    }

    public void populateStudentTable() throws SQLException{

        try (Connection connection = conn.getConnection()) {
            String sql = "INSERT INTO Student (name, age) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "John Doe");
                statement.setInt(2, 20);
                statement.executeUpdate();

                statement.setString(1, "Jane Smith");
                statement.setInt(2, 22);
                statement.executeUpdate();

                statement.setString(1, "Mike Brown");
                statement.setInt(2, 21);
                statement.executeUpdate();

            }
        }
    }
}

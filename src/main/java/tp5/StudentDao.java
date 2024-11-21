package tp5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao {
    private final DatabaseConnection connection;

    public StudentDao(DatabaseConnection conn) {
        this.connection = conn;
    }

    /**
     * Retrieves all student records from the database.
     *
     * @return a list of all students from the database
     * @throws SQLException if an SQL error occurs during the operation
     */
    public List<Student> findAll() throws SQLException {
        try (Connection conn = connection.getConnection();
             Statement statement = conn.createStatement()) {
            var rs = statement.executeQuery("SELECT * FROM student");
            var students = new ArrayList<Student>();
            while (rs.next()) {
                var student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"));
                students.add(student);
            }
            return students;
        }
    }


    /**
     * Retrieves a student by their unique student ID from the database.
     *
     * @param studentId the ID of the student to be retrieved
     * @return an Optional containing the Student if found, or an empty Optional if no student is found
     * @throws SQLException if an SQL error occurs during the operation
     */
    public Optional<Student> findById(int studentId) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new Student(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("age")));
            } else {
                // use Optional if you know it
                return Optional.empty();
            }
        }
    }

    /**
     * Inserts a new student into the database.
     *
     * @param student the student data to be inserted, which includes the name and age
     * @return the generated ID of the newly inserted student
     * @throws SQLException if an SQL error occurs during the operation
     */
    public int create(StudentData student) throws SQLException {
        String sql = "INSERT INTO student (name, age) VALUES (?, ?)";
        try (Connection conn = connection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, student.name());
            preparedStatement.setInt(2, student.age());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting student failed, no ID obtained.");
                }
            }
        }
    }


    /**
     * Updates a student's information in the database based on the provided ID.
     *
     * @param id the ID of the student to update
     * @param newData the new data for the student, which includes the updated name and age
     * @return the number of rows affected by the update operation
     * @throws SQLException if an SQL error occurs during the operation
     */
    public int update(int id, StudentData newData) throws SQLException {
        String sql = "UPDATE student SET name = ?, age = ? WHERE id = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newData.name());
            preparedStatement.setInt(2, newData.age());
            preparedStatement.setInt(3, id);
            return preparedStatement.executeUpdate();
        }
    }

    /**
     * Deletes a student from the database based on the provided ID.
     *
     * @param id the ID of the student to delete
     * @return the number of rows affected by the delete operation
     * @throws SQLException if an SQL error occurs during the operation
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        }
    }


}

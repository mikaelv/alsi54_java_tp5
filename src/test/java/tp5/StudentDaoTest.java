package tp5;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentDaoTest {
    // in-memory database. DB_CLOSE_DELAY=-1 means that the DB is not cleared after connection.close()
    public static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    public static final String USER = "sa";
    public static final String PASSWORD = "";


    private final StudentDao dao = new StudentDao(new DatabaseConnection(URL, USER, PASSWORD));

    @BeforeAll
    public static void setUp() throws SQLException {
        DatabaseConnection conn = new DatabaseConnection(URL, USER, PASSWORD);
        CreateTable createTable = new CreateTable(conn);
        createTable.createStudentTable();
        PopulateTable populateTable = new PopulateTable(conn);
        populateTable.populateStudentTable();
    }


    @Test
    @Order(0)
    void testSetup() throws SQLException {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            var rs = statement.executeQuery("SELECT count(*) FROM student");
            rs.next();
            count = rs.getInt(1);
            assertEquals(3, count);
        }
    }


    @Test
    @Order(1)
        // !! The test is not independent: it might fail if in the wrong order, or in parallel with other tests
    void findAll() throws SQLException {
        // GIVEN a populated student table
        // WHEN
        var actualStudents = dao.findAll();
        // THEN
        var expectedStudents = List.of(
                new Student(2, "Jane Smith", 22),
                new Student(1, "John Doe", 20),
                new Student(3, "Mike Brown", 21));
        // Use HashSet because they might be in a different order
        Assertions.assertIterableEquals(new HashSet<>(expectedStudents), new HashSet<>(actualStudents));
    }

    @Test
    @Order(2)
    void findById() throws SQLException {
        // GIVEN
        var studentId = 1;
        // WHEN
        var actualStudent = dao.findById(studentId);
        // THEN
        var expectedStudent = new Student(1, "John Doe", 20);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
    }

    @Test
    @Order(3)
    void create() throws SQLException {
        // GIVEN
        var newStudent = new StudentData("Alice White", 23);
        var newId = dao.create(newStudent);

        // WHEN
        var actualStudent = dao.findById(newId);

        // THEN
        assertTrue(actualStudent.isPresent());
        assertEquals(newStudent.name(), actualStudent.get().name());
        assertEquals(newStudent.age(), actualStudent.get().age());
    }


    @Test
    @Order(4)
    void update() throws SQLException {
        // GIVEN
        var originalStudentId = 1;
        var newStudentData = new StudentData("Johnny Doe", 21);

        // WHEN
        int rowsUpdated = dao.update(originalStudentId, newStudentData);

        // THEN
        assertEquals(1, rowsUpdated);
        var updatedStudent = dao.findById(originalStudentId);
        assertTrue(updatedStudent.isPresent());
        assertEquals(originalStudentId, updatedStudent.get().id());
        assertEquals(newStudentData.name(), updatedStudent.get().name());
        assertEquals(newStudentData.age(), updatedStudent.get().age());
    }


    @Test
    @Order(5)
    void delete() throws SQLException {
        // GIVEN
        var studentIdToDelete = 1;
        var studentBeforeDeletion = dao.findById(studentIdToDelete);
        assertTrue(studentBeforeDeletion.isPresent());

        // WHEN
        int rowsDeleted = dao.delete(studentIdToDelete);

        // THEN
        assertEquals(1, rowsDeleted);
        var studentAfterDeletion = dao.findById(studentIdToDelete);
        assertTrue(studentAfterDeletion.isEmpty());
    }
}


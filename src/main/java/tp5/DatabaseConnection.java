package tp5;

public class DatabaseConnection {

    private String url;
    private String username;
    private String password;

    public DatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public java.sql.Connection getConnection() throws java.sql.SQLException {
        return java.sql.DriverManager.getConnection(url, username, password);
    }
}

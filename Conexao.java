package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Added for fechar method
import java.sql.SQLException;
import java.sql.Statement; // Added for fechar method

public class Conexao {

    // Database connection details for SQL Server
    // IMPORTANT: 'gugu*123' is a strong password. Ensure you keep it secure.
    // Setting encrypt=true and trustServerCertificate=true is for development convenience.
    // For production, consider importing the server's SSL certificate into your JVM's truststore.
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Biblioteca;encrypt=true;trustServerCertificate=true;";
    private static final String USUARIO = "sa"; // Your SQL Server username
    private static final String SENHA = "gugu*123"; // Your SQL Server password

    // Static block to load the SQL Server JDBC driver
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver JDBC do SQL Server carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do SQL Server não encontrado. Certifique-se de que o JAR do driver está no classpath.");
            e.printStackTrace();
            // Exit the application if the driver isn't found, as it's a critical dependency
            System.exit(1);
        }
    }

    /**
     * Establishes and returns a database connection to SQL Server.
     * @return A Connection object if successful, null otherwise.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados SQL Server estabelecida!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }

    /**
     * Closes database resources (Connection, Statement, ResultSet).
     * Handles null checks for robustness.
     * @param conn The Connection object to close.
     * @param stmt The Statement (or PreparedStatement) object to close.
     * @param rs The ResultSet object to close.
     */
    public static void fechar(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

	public static void closeConnection(Connection conn, PreparedStatement stmtLivro, ResultSet rs) {
		// TODO Auto-generated method stub
		
	}
}
package eu.surething_project.core.database;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public DatabaseConnection() { }

    public Connection connectToDatabase() {
        Connection connection = null;
        String timezone = "?serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VerifierDB" +
                            timezone, "miguel", "123456");
        } catch (ClassNotFoundException e) {
            throw new VerifierException(ErrorMessage.DATABASE_CONNECTION_EXCEPTION, e);
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.DATABASE_CONNECTION_EXCEPTION, e);
        }

        return connection;

    }
}

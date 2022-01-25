package eu.surething_project.core.database;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    protected DatabaseConnection() {
    }

    protected Connection connectToDatabase() {
        Connection connection;
        String timezone = "?serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/verifier_data"
                    + timezone, "verifieruser", "12345678");
        } catch (ClassNotFoundException e) {
            throw new VerifierException(ErrorMessage.DATABASE_CONNECTION_EXCEPTION, e);
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.DATABASE_CONNECTION_EXCEPTION, e);
        }

        return connection;

    }
}

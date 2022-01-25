package eu.surething_project.core.database;

import eu.surething_project.core.database.data.LocationClaimData;
import eu.surething_project.core.database.data.LocationEndorsementData;
import eu.surething_project.core.database.data.LocationProofData;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.sql.*;
import java.util.List;

public class DatabaseAccessManagement {

    private DatabaseConnection dbConnection;

    /**** Table names ****/
    private static final String PROOFS_TABLE_NAME = "Proofs";
    private static final String ENDORSEMENTS_TABLE_NAME = "Endorsements";
    private static final String CLAIMS_TABLE_NAME = "Claims";

    /****************+** Database Queries ****************+**/
    /**** Create Tables ****/
    private static final String CREATE_PROOFS_TABLE_SQL = "CREATE TABLE " + PROOFS_TABLE_NAME + " (" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "proofId VARCHAR(36) NOT NULL," +
            "timeInMillis LONG NOT NULL," +
            "PRIMARY KEY(id)," +
            "UNIQUE (proofId))";
    private static final String CREATE_ENDORSEMENT_TABLE_SQL = "CREATE TABLE " + ENDORSEMENTS_TABLE_NAME + "(" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "endorsementId VARCHAR(36) NOT NULL," +
            "witnessId VARCHAR(8) NOT NULL," +
            "claimId VARCHAR(36) NOT NULL," +
            "latitude DOUBLE NOT NULL," +
            "longitude DOUBLE NOT NULL," +
            "timeInMillis LONG NOT NULL," +
            "proofId VARCHAR(36) NOT NULL," +
            "PRIMARY KEY(id)," +
            "FOREIGN KEY (proofId) REFERENCES Proofs(proofId))";
    private static final String CREATE_CLAIMS_TABLE_SQL = "CREATE TABLE " + CLAIMS_TABLE_NAME + "(" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "claimId VARCHAR(36) NOT NULL," +
            "proverId VARCHAR(36) NOT NULL," +
            "latitude DOUBLE NOT NULL," +
            "longitude DOUBLE NOT NULL," +
            "timeInSeconds DOUBLE NOT NULL," +
            "proofId VARCHAR(36) NOT NULL," +
            "PRIMARY KEY(id)," +
            "FOREIGN KEY (proofId) REFERENCES Proofs(proofId))";

    /**** Update Tables ****/
    private static final String INSERT_PROOF_SQL = "INSERT INTO Proofs (proofId, timeInMillis) VALUES (?, ?)";
    private static final String INSERT_ENDORSE_SQL = "INSERT INTO Endorsements (endorsementId, witnessId, claimId, " +
            "latitude, longitude, timeInMillis, proofId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CLAIM_SQL = "INSERT INTO Claims (claimId, proverId, latitude, " +
            "longitude, timeInSeconds, proofId) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_PROOF_BY_ID_SQL = "SELECT proofId, timeInMillis FROM Proofs WHERE (proofId = ?)";
    private static final String GET_ENDORSEMENT_BY_ID_SQL = "SELECT * FROM Endorsements WHERE (endorsementId = ?);";
    private static final String GET_CLAIM_BY_ID_SQL = "SELECT * FROM Claims WHERE (claimId = ?);";
    private static final String GET_CLAIM_BY_PROVER_ID_SQL = "SELECT claimId, proverId, latitude, " +
            "longitude, timeInSeconds, proofId " +
            "FROM Claims c1 " +
            "WHERE (proverId = ?)" +
            "AND timeInSeconds = (SELECT MAX(timeInSeconds)" +
            "   FROM Claims c2 " +
            "   WHERE c2.proverId = c1.proverId)";

    public DatabaseAccessManagement() {
        this.dbConnection = new DatabaseConnection();
        createAllTables();
    }

    /**
     * Creates all SQL Tables
     */
    private void createAllTables() {
        createTable(PROOFS_TABLE_NAME, CREATE_PROOFS_TABLE_SQL,
                ErrorMessage.ERROR_CREATING_PROOFS_TABLE);
        createTable(ENDORSEMENTS_TABLE_NAME, CREATE_ENDORSEMENT_TABLE_SQL,
                ErrorMessage.ERROR_CREATING_ENDORSEMENTS_TABLE);
        createTable(CLAIMS_TABLE_NAME, CREATE_CLAIMS_TABLE_SQL,
                ErrorMessage.ERROR_CREATING_CLAIMS_TABLE);
    }

    /**
     * Creates a MySQL table
     *
     * @param sqlStatement - Statement to execute
     * @param errorMessage - message to display on error
     */
    private void createTable(String tableName, String sqlStatement,
                             ErrorMessage errorMessage) {
        // Return if table already exists
        if (tableExists(tableName)) {
            return;
        }

        Connection connection = dbConnection.connectToDatabase();
        Statement createProofTable = null;
        try {
            createProofTable = connection.createStatement();
            createProofTable.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            throw new VerifierException(errorMessage, e);
        } finally {
            closeConnection(connection);
            closeStatement(createProofTable);
        }
    }

    public void addProofData(LocationProofData proof) {
        PreparedStatement updateProof = null;
        PreparedStatement updateEndorsements = null;
        PreparedStatement updateClaims = null;
        Connection connection = dbConnection.connectToDatabase();

        try {
            updateProof = connection.prepareStatement(INSERT_PROOF_SQL);
            updateProof.setString(1, proof.getProofId());
            updateProof.setLong(2, proof.getTimeInSeconds());
            updateProof.executeUpdate();

            // Add locationEndorsements
            List<LocationEndorsementData> endorsementList = proof.getEndorsements();
            updateEndorsements = connection.prepareStatement(INSERT_ENDORSE_SQL);
            for (LocationEndorsementData locEndorse : endorsementList) {
                updateEndorsements.setString(1, locEndorse.getEndorsementId());
                updateEndorsements.setString(2, locEndorse.getWitnessId());
                updateEndorsements.setString(3, locEndorse.getClaimId());
                updateEndorsements.setDouble(4, locEndorse.getLatitude());
                updateEndorsements.setDouble(5, locEndorse.getLongitude());
                updateEndorsements.setLong(6, locEndorse.getTimeInMillis());
                updateEndorsements.setString(7, proof.getProofId()); // Foreign key
                updateEndorsements.addBatch();
            }
            int[] res = updateEndorsements.executeBatch();

            // Add Location Claim
            LocationClaimData claim = proof.getClaim();
            updateClaims = connection.prepareStatement(INSERT_CLAIM_SQL);
            updateClaims.setString(1, claim.getClaimId());
            updateClaims.setString(2, claim.getProverId());
            updateClaims.setDouble(3, claim.getLatitude());
            updateClaims.setDouble(4, claim.getLongitude());
            updateClaims.setDouble(5, claim.getTimeInSeconds());
            updateClaims.setString(6, proof.getProofId()); // Foreign key
            updateClaims.executeUpdate();
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_ACCESSING_SQL_TABLE, e);
        } finally {
            closeConnection(connection);
            closeStatement(updateProof);
            closeStatement(updateEndorsements);
            closeStatement(updateClaims);
        }
    }

    /**
     * Get most recent claim
     *
     * @param proverId
     * @return
     */
    public LocationClaimData getLastClaimByProverId(String proverId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = dbConnection.connectToDatabase();
        LocationClaimData claimData = null;
        try {
            stmt = connection.prepareStatement(GET_CLAIM_BY_PROVER_ID_SQL);
            stmt.setString(1, proverId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                claimData = new LocationClaimData(rs.getString(1), rs.getString(2),
                        rs.getDouble(3), rs.getDouble(4), rs.getLong(5),
                        rs.getString(6));
            }
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_ACCESSING_SQL_TABLE, e);
        } finally {
            closeConnection(connection);
            closeStatement(stmt);
            closeResultSet(rs);
        }

        return claimData;
    }

    public LocationProofData getProofById(String id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = dbConnection.connectToDatabase();
        LocationProofData proofData = null;
        try {
            stmt = connection.prepareStatement(GET_PROOF_BY_ID_SQL);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                proofData = new LocationProofData(rs.getString(1), rs.getLong(2));
            }
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_ACCESSING_SQL_TABLE, e);
        } finally {
            closeConnection(connection);
            closeStatement(stmt);
            closeResultSet(rs);
        }
        return proofData;
    }

    /**
     * Checks if a MySQL Table exists
     *
     * @param tableName
     * @return
     */
    private boolean tableExists(String tableName) {
        Connection connection = dbConnection.connectToDatabase();
        ResultSet rs = null;
        boolean tExists = false;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(null, null, tableName,
                    new String[]{"TABLE"});
            tExists = rs.next();
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_VERIFYING_TABLE, e);
        } finally {
            closeConnection(connection);
            closeResultSet(rs);
        }
        return tExists;
    }

    /**
     * Close connection
     *
     * @param connection - Connection to close
     */
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
            }
        }
    }

    /**
     * Close Statement
     *
     * @param stmt - Statement to close
     */
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_STATEMENT, e);
            }
        }
    }

    /**
     * Close ResultSet
     *
     * @param resultSet - ResultSet to close
     */
    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new VerifierException(ErrorMessage.ERROR_CLOSING_RESULTSET, e);
            }
        }
    }
}

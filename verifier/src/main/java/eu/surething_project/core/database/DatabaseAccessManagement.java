package eu.surething_project.core.database;

import com.google.protobuf.InvalidProtocolBufferException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.LocationClaim;
import eu.surething_project.core.grpc.LocationEndorsement;
import eu.surething_project.core.grpc.LocationProof;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.grpc.google.type.LatLng;

import java.sql.*;
import java.util.List;

public class DatabaseAccessManagement {

    private DatabaseConnection dbConnection;

    /**** Table names ****/
    private static final String PROOFS_TABLE_NAME = "Proofs";
    private static final String ENDORSEMENTS_TABLE_NAME = "Proofs";
    private static final String CLAIMS_TABLE_NAME = "Proofs";

    /****************+** Database Queries ****************+**/
    /**** Create Tables ****/
    private static final String CREATE_PROOFS_TABLE_SQL = "CREATE TABLE" + PROOFS_TABLE_NAME + " (" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "proofId VARCHAR(36) NOT NULL," +
            "timeInMillis LONG NOT NULL," +
            "PRIMARY KEY(id))";
    private static final String CREATE_ENDORSEMENT_TABLE_SQL = "CREATE TABLE" + ENDORSEMENTS_TABLE_NAME + "(" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "endorsementId VARCHAR(36) NOT NULL," +
            "witnessId VARCHAR(8) NOT NULL," +
            "claimId VARCHAR(36) NOT NULL," +
            "latitude DOUBLE NOT NULL," +
            "longitude DOUBLE NOT NULL," +
            "timeInMillis LONG NOT NULL," +
            "proofId VARCHAR(36) NOT NULL," +
            "PRIMARY KEY(id)," +
            "FOREIGN KEY(proofId) REFERENCES Proofs)";
    private static final String CREATE_CLAIMS_TABLE_SQL = "CREATE TABLE" + CLAIMS_TABLE_NAME + "(" +
            "id INT(64) NOT NULL AUTO_INCREMENT," +
            "claimId VARCHAR(36) NOT NULL," +
            "proverId VARCHAR(36) NOT NULL," +
            "latitude DOUBLE NOT NULL," +
            "longitude DOUBLE NOT NULL," +
            "timeInMillis LONG NOT NULL," +
            "proofId VARCHAR(36) NOT NULL," +
            "PRIMARY KEY(id)," +
            "FOREIGN KEY(proofId) REFERENCES Proofs)";

    /**** Update Tables ****/
    private static final String INSERT_PROOF_SQL = "INSERT INTO Proofs (proofId, timeInMillis) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ENDORSE_SQL = "INSERT INTO Endorsements (endorsementId, witnessId, claimId, " +
            "latitude, longitude, timeInMillis, proofId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CLAIM_SQL = "INSERT INTO Claims (claimId, proverId, latitude, " +
            "longitude, timeInMillis, proofId) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_PROOF_BY_ID_SQL = "SELECT * FROM Proofs WHERE (proofId = ?);";
    private static final String GET_ENDORSEMENT_BY_ID_SQL = "SELECT * FROM Endorsements WHERE (endorsementId = ?);";
    private static final String GET_CLAIM_BY_ID_SQL = "SELECT * FROM Claims WHERE (claimId = ?);";
    private static final String GET_CLAIM_BY_PROVER_ID_SQL = "SELECT * FROM Claims WHERE (proverId = ?) AND " +
            "timeInMillis = (SELECT MAX(timeInMillis) FROM Claims);";

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
    private void createTable(String tableName, String sqlStatement, ErrorMessage errorMessage) {
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
            throw new VerifierException(errorMessage);
        } finally {
            closeConnection(connection);
            closeStatement(createProofTable);
        }
    }

    public void addProofToDB(LocationProof proof) {
        PreparedStatement updateProof = null;
        PreparedStatement updateEndorsements = null;
        PreparedStatement updateClaims = null;
        Connection connection = dbConnection.connectToDatabase();
        try {
            updateProof = connection.prepareStatement(INSERT_PROOF_SQL);
            updateProof.setString(1, proof.getProofId());
            updateProof.setString(2, proof.getTime().toString());
            updateProof.executeUpdate();

            // Add locationEndorsements
            List<SignedLocationEndorsement> endorsementList = proof.getLocationEndorsementsList();
            for (SignedLocationEndorsement endorsement : endorsementList) {
                LocationEndorsement locEndorse = endorsement.getEndorsement();
                LatLng latLng = locEndorse.getEvidence().unpack(LatLng.class);
                updateEndorsements = connection.prepareStatement(INSERT_ENDORSE_SQL);
                updateEndorsements.setString(1, locEndorse.getEndorsementId());
                updateEndorsements.setString(2, locEndorse.getWitnessId());
                updateEndorsements.setString(3, locEndorse.getClaimId());
                updateEndorsements.setDouble(4, latLng.getLatitude());
                updateEndorsements.setDouble(5, latLng.getLongitude());
                updateEndorsements.setLong(6, locEndorse.getTime().getRelativeToEpoch().getTimeValue());
                updateEndorsements.setString(7, proof.getProofId()); // Foreign key
                updateEndorsements.addBatch();
            }
            updateEndorsements.executeBatch();

            // Add Location Claim
            LocationClaim claim = proof.getLocClaim();
            LatLng latLng = claim.getEvidence().unpack(LatLng.class);
            updateClaims = connection.prepareStatement(INSERT_CLAIM_SQL);
            updateClaims.setString(1, claim.getClaimId());
            updateClaims.setString(2, claim.getProverId());
            updateClaims.setDouble(3, latLng.getLatitude());
            updateClaims.setDouble(4, latLng.getLongitude());
            updateClaims.setLong(5, claim.getTime().getRelativeToEpoch().getTimeValue());
            updateClaims.setString(6, proof.getProofId()); // Foreign key
            updateClaims.executeUpdate();
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_ACCESSING_SQL_TABLE, e);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
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
    public LocationClaimData getGetClaimByProverId(String proverId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = dbConnection.connectToDatabase();
        LocationClaimData claimData;
        try {
            stmt = connection.prepareStatement(GET_CLAIM_BY_PROVER_ID_SQL);
            stmt.setString(1, proverId);
            rs = stmt.executeQuery();

            rs.next();
            claimData = new LocationClaimData(rs.getString(1), rs.getString(2),
                    rs.getDouble(3), rs.getDouble(4), rs.getLong(5),
                    rs.getString(6));
        } catch (SQLException e) {
            throw new VerifierException(ErrorMessage.ERROR_ACCESSING_SQL_TABLE, e);
        } finally {
            closeConnection(connection);
            closeStatement(stmt);
            closeResultSet(rs);
        }

        return claimData;
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
        try {
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(null, null, "My_Table_Name",
                    new String[]{"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
            closeResultSet(rs);
        }
        return false;
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

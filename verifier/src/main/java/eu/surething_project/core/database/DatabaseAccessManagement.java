package eu.surething_project.core.database;

import com.google.protobuf.InvalidProtocolBufferException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.LocationClaim;
import eu.surething_project.core.grpc.LocationEndorsement;
import eu.surething_project.core.grpc.LocationProof;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.grpc.google.type.LatLng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseAccessManagement {

    private DatabaseConnection dbConnection;

    /****************+** Database Queries ****************+**/
    private static final String INSERT_PROOF_SQL = "INSERT INTO Proofs (proofId, time) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ENDORSE_SQL = "INSERT INTO Endorsements (endorsementId, witness, claimId, latitude, " +
            "longitude, timeInMillis, proofId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CLAIM_SQL = "INSERT INTO Claims (claimId, proverId, latitude, " +
            "longitude, timeInMillis, proofId) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_PROOF_BY_ID_SQL = "SELECT * FROM Proofs WHERE (proofId = ?);";
    private static final String GET_ENDORSEMENT_BY_ID_SQL = "SELECT * FROM Endorsements WHERE (endorsementId = ?);";
    private static final String GET_CLAIM_BY_ID_SQL = "SELECT * FROM Claims WHERE (claimId = ?);";
    private static final String GET_CLAIM_BY_PROVER_ID_SQL = "SELECT * FROM Claims WHERE (proverId = ?) AND " +
            "timeInMillis = (SELECT MAX(timeInMillis) FROM Claims);";

    public DatabaseAccessManagement() {
        this.dbConnection = new DatabaseConnection();
    }

    public void addToDatabase(LocationProof proof) {
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
            if (updateProof != null) {
                try {
                    updateProof.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
            if (updateEndorsements != null) {
                try {
                    updateEndorsements.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
            if (updateClaims != null) {
                try {
                    updateClaims.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new VerifierException(ErrorMessage.ERROR_CLOSING_DB_CONNECTION, e);
                }
            }
        }

        return claimData;
    }
}

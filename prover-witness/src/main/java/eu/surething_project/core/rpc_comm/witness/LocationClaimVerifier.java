package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationClaim;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;

import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;

public class LocationClaimVerifier {

    private LocationEndorsementBuilder endorsementBuilder;

    private CryptoHandler cryptoHandler;

    private String externalData;

    public LocationClaimVerifier(CryptoHandler cryptoHandler, String certPath, String externalData,
                                 String witnessId, Entity currEntity) {
        this.endorsementBuilder = new LocationEndorsementBuilder(cryptoHandler, certPath,
                witnessId, currEntity);
        this.cryptoHandler = cryptoHandler;
        this.externalData = externalData;
    }

    /**
     * Verifies the location claim data sent by the prover
     *
     * @param signedLocationClaim
     * @return
     */
    public SignedLocationEndorsement verifyLocationClaim(SignedLocationClaim signedLocationClaim)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            UnrecoverableKeyException, KeyStoreException, FileNotFoundException, CertificateException,
            NoSuchProviderException {

        // Get signed data
        Signature signature = signedLocationClaim.getProverSignature();
        String externalEntityId = signedLocationClaim.getClaim().getProverId();
        long nonce = signature.getNonce();
        byte[] signedClaim = signature.getValue().toByteArray();
        byte[] certData = signature.getCertificateData().toByteArray();
        String cryptoAlg = signature.getCryptoAlgo();

        // Get LocationClaim data
        LocationClaim locClaim = signedLocationClaim.getClaim();

        // Create Certificate if necessary
        boolean certFileCreate = CertificateAccess.createCertificateFile(externalData,
                externalEntityId, certData);
        if (!certFileCreate) {
            throw new EntityException(ErrorMessage.ERROR_CREATING_CERTIFICATE);
        }

        // create Certificate and verify validity
        cryptoHandler.verifyCertificate(externalEntityId);

        // Verify signed data
        cryptoHandler.verifyData(locClaim.toByteArray(), signedClaim, externalEntityId, cryptoAlg);

        // Start Data content verification
        String claimId = locClaim.getClaimId();

        // Build Signed endorsement
        SignedLocationEndorsement signedEndorsement = this.endorsementBuilder
                .buildSignedLocationEndorsement(claimId, nonce, cryptoAlg);

        // DEBUG
        if (PropertiesReader.getDebugProperty()) {
            System.out.println(" -------- \nReceived Claim from Prover with claimId: " + locClaim.getClaimId());
            System.out.println("Sending Endorsement with ID: " +
                    signedEndorsement.getEndorsement().getEndorsementId());
            System.out.print("\n>> ");
        }

        return signedEndorsement;
    }

}

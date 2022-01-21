package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationEndorsement;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.SignedLocationEndorsement;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

public class LocationEndorsementVerifier {

    public static void verifyEndorsement(CryptoHandler cryptoHandler, long sentNonce,
                                         SignedLocationEndorsement signedLocationEndorsement)
            throws FileNotFoundException, CertificateException, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException {
        // Get signed data
        Signature signature = signedLocationEndorsement.getWitnessSignature();
        String externalEntityId = signedLocationEndorsement.getEndorsement().getWitnessId();
        byte[] signedEndorsement = signature.getValue().toByteArray();
        String cryptoAlg = signature.getCryptoAlgo();

        // Get LocationEndorsement data
        LocationEndorsement endorsement = signedLocationEndorsement.getEndorsement();

        // Verify signed data
        cryptoHandler.verifyData(endorsement.toByteArray(), signedEndorsement, externalEntityId, cryptoAlg);

        // Verify Nonce
        long nonce = signature.getNonce();
        if (nonce != sentNonce) {
            throw new EntityException(ErrorMessage.NONCE_MATCH_ERROR);
        }
    }
}

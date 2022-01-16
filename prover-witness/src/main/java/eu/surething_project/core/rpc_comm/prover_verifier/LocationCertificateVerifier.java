package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.SignedLocationEndorsement;

public class LocationCertificateVerifier {

    public static void verifyCertificate(long sentNonce, LocationCertificate locationCertificate) {
        // Get nonce
        Signature signature = locationCertificate.getVerifierSignature();
        long nonce = signature.getNonce();
        if (nonce != sentNonce) {
            throw new EntityException(ErrorMessage.NONCE_MATCH_ERROR);
        }
    }
}

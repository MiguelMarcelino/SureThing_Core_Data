package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.SignedLocationEndorsement;

public class LocationEndorsementVerifier {

    public static void verifyEndorsement(long sentNonce, SignedLocationEndorsement signedLocationEndorsement) {
        // Get nonce
        Signature signature = signedLocationEndorsement.getWitnessSignature();
        long nonce = signature.getNonce();
        if (nonce != sentNonce) {
            throw new EntityException(ErrorMessage.NONCE_MATCH_ERROR);
        }
    }
}

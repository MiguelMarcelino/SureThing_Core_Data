package eu.surething_project.core.rpc_comm;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import eu.surething_project.core.grpc.*;

/**
 * Communicates with the Verifier and expects a LocationCertificate
 */
public class CertifyClaimService extends CertifyClaim {

    /**
     * This method sends a claim to the verifier
     * @param controller -
     * @param request - sends a signed location request
     * @param done - receives a location Certificate
     */
    @Override
    public void sendClaimToVerifier(RpcController controller, SignedLocationEndorsement request, RpcCallback<LocationCertificate> done) {
        // TODO
    }

    /**
     * This method has no signing (Just for Testing communication)
     * @param controller
     * @param request
     * @param done
     */
    @Override
    public void sendClaimToVerifierNoSigning(RpcController controller, LocationEndorsement request, RpcCallback<LocationVerification> done) {

    }
}

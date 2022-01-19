package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.EndorseClaimGrpc;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 */
public class WitnessClient {

    private final EndorseClaimGrpc.EndorseClaimBlockingStub blockingStub;

    private final EndorseClaimGrpc.EndorseClaimStub asyncStub;

    public WitnessClient(ManagedChannel channel) {
        blockingStub = EndorseClaimGrpc.newBlockingStub(channel);
        asyncStub = EndorseClaimGrpc.newStub(channel);
    }

    /**
     * Sends a location claim to a witness
     * @param locationClaim
     */
    public SignedLocationEndorsement sendSignedClaimToWitness(SignedLocationClaim locationClaim) {
        SignedLocationEndorsement signedLocationEndorsement;
        try {
            signedLocationEndorsement = blockingStub.checkClaim(locationClaim);
        } catch (StatusRuntimeException e) {
            throw new EntityException(ErrorMessage.LOCATION_CLAIM_SEND_ERROR);
        }

        return signedLocationEndorsement;
    }

}

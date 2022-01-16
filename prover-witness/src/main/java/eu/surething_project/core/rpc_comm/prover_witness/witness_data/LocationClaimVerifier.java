package eu.surething_project.core.rpc_comm.prover_witness.witness_data;

import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import org.springframework.stereotype.Service;

@Service
public class LocationClaimVerifier {

    private LocationEndorsementBuilder endorsementBuilder;
    
    public LocationClaimVerifier() {
        this.endorsementBuilder = new LocationEndorsementBuilder();
    }

    /**
     * Verifies if a location claim is sent from the entity claiming to be the prover
     * @param locationClaim
     * @return
     */
    public SignedLocationEndorsement verifyLocationClaim(SignedLocationClaim locationClaim) {
        // TODO
        return null;
    }
}

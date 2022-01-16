package eu.surething_project.core.rpc_comm;

import com.google.protobuf.BlockingRpcChannel;
import eu.surething_project.core.grpc.CertifyClaim;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VerifierClient {
    private static final Logger logger = LoggerFactory.getLogger(VerifierClient.class);

    private final CertifyClaim.BlockingInterface blockingStub;
    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaim.newBlockingStub(channel);
    }

    public void sendClaimToVerifier() {

    }

}
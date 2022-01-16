package eu.surething_project.core;

import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServerHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootApplication
public class ProverApplication implements InitializingBean {

	@Autowired
	private WitnessGrpcServerHandler grpcServerHandler;

	// TODO:
	// - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
	// - Using nonce for freshness verification (probably before RPC begins)
	public static void main(String[] args) {
		// Test

//		SpringApplication.run(ProverApplication.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		grpcServerHandler.buildServer();
	}
}

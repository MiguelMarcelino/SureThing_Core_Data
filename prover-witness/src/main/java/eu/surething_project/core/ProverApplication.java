package eu.surething_project.core;

import eu.surething_project.core.rpc_comm.prover_witness.GrpcServerHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootApplication
public class ProverApplication implements InitializingBean {

	@Autowired
	private GrpcServerHandler grpcServerHandler;

	// TODO:
	// - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
	// - Using nonce for freshness verification (probably before RPC begins)
	public static void main(String[] args) {

//		SpringApplication.run(ProverApplication.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		grpcServerHandler.buildServer();
	}
}

package eu.surething_project.core;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.rpc_comm.GrpcServer;
import eu.surething_project.core.rpc_comm.GrpcServerHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class VerifierApplication implements InitializingBean {

	@Autowired
	private GrpcServerHandler grpcServerHandler;

	public static void main(String[] args) {
		SpringApplication.run(VerifierApplication.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		grpcServerHandler.buildServer();
	}
}

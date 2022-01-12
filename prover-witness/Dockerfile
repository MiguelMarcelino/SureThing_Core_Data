FROM openjdk:8

# Setting Work directory in image
WORKDIR /app/prover

# Get port from command line args at build time and expose it
ARG PORT=8080
EXPOSE $PORT

# Copy the Jar file to the prover working directory
COPY ./target/prover-0.0.1-SNAPSHOT.jar /app/prover

# Running jar in command line
CMD ["java", "-jar", "prover-0.0.1-SNAPSHOT.jar"]

# Alternative for testing
# CMD ["./mvnw", "spring-boot:run"]

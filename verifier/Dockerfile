FROM openjdk:8

# Setting Work directory in image
WORKDIR /app/verifier

# Get port from command line args at build time and expose it
ARG PORT=8080
EXPOSE $PORT

# Copy the Jar file to the verifier working directory
COPY ./target/verifier-0.0.1-SNAPSHOT.jar /app/verifier

# Running jar in command line
CMD ["java", "-jar", "verifier-0.0.1-SNAPSHOT.jar"]

# Alternative for testing
# CMD ["./mvnw", "spring-boot:run"]

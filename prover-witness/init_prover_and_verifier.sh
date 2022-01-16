#!/bin.sh
mvn clean -Pproduction package
docker build --build-arg PORT=8080 -t prover .
docker build --build-arg PORT=8081 -t verifier .


sudo docker run -d -p 8080:8080 --name prover_app prover
sudo docker run -d -p 8081:8081 --name witness_app witness

# To restart: sudo docker start prover_app
# To stop: sudo docker stop prover_app
# To remove: sudo docker rm prover_app
# Check if image was removed: sudo docker ps -a


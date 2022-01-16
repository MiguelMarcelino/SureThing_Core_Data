#!/bin.sh
mvn clean -Pproduction package
docker build --build-arg PORT=8082 -t verifier .


sudo docker run -d -p 8082:8082 --name verifier_app verifier
# To restart: sudo docker start prover_app
# To stop: sudo docker stop prover_app
# To remove: sudo docker rm prover_app
# Check if image was removed: sudo docker ps -a


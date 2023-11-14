# demo-rabbitmq
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 -p 8080:8080 -v .\demo-rabbitmq:\demo rabbitmq:3.12-management
docker exec -it rabbitmq bash
apt update
apt upgrade
apt install default-jdk
#!/bin/bash

cmd=$(basename $0)

RUNNING_SERVICE_VERSION=$(sudo docker inspect --format='$%index (index .Config.Env) 2%$' $1 | tr -d \PROJECT_VERSION=)

if [[ "${RUNNING_SERVICE_VERSION}" != "$2" ]]; then
   echo "rollback to version $2}"
   sudo docker pull "$3"

   if [[ "$1" == "configuration-service" ]]; then
        cat >$1.yml << EOF
version: '3.2'

volumes:
 gnoc_logs: {}

services:
 $1:
    build:
      context: ./
      args:
        PROJECT_VERSION: $2
        JDK_BASE_IMAGE: $4
    image: "$3"
    container_name: "$1"
    ports: ['$5:$5']
    network_mode: "host"
    volumes: ['gnoc_logs:/app/log']
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    entrypoint: java -Xms$6m -Xmx$7m -Djava.security.egd=file:///dev/urandom -jar /app/$1.jar
    command: ["--spring.profiles.active=$8,$9", "--spring.cloud.config.server.git.uri=${10}", "--spring.cloud.config.label=${11}", "--server.port=$5"]
    healthcheck:
      interval: 5s
      timeout: 3s
      retries: 20
      test: ["CMD", "curl", "-f", "http://localhost:$5/actuator/health"]
    restart: always
EOF
   else
        cat >$1.yml << EOF
version: '3.2'

volumes:
  gnoc_logs: {}

services:
 $1:
    build:
      context: ./
      args:
        PROJECT_VERSION: $2
        JDK_BASE_IMAGE: $4
    image: "$3"
    container_name: "$1"
    ports: ['$5:$5']
    network_mode: "host"
    volumes: ['gnoc_logs:/app/log']
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    entrypoint: java -Xms$6m -Xmx$7m -Djava.security.egd=file:///dev/urandom -jar /app/$1.jar
    command: ["--spring.cloud.config.uri=${12}", "--spring.cloud.config.server.git.uri=${10}", "--spring.cloud.config.label=${11}", "--spring.profiles.active=$8", "--server.port=$5"]
    healthcheck:
      interval: 5s
      timeout: 3s
      retries: 20
      test: ["CMD", "curl", "-f", "http://localhost:$5/actuator/health"]
    restart: always
EOF
   fi
   sudo docker-compose --verbose -p gnoc -f $1.yml up -d
else
    echo "Don't need to rollback!"
    exit 1
fi


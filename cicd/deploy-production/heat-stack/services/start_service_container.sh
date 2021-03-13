#!/bin/bash

cd $%working_dir%$

service_docker_image="$%service_docker_image_repo%$/$%service_name%$:$%service_version%$"

echo "Config: $%service_docker_image%$"

docker pull $%service_docker_image%$

echo "Config: $%cloud_config_uri%$"

if [[ "$%service_name%$" == "configuration-service" ]]; then
cat >$%service_name%$.yml <<EOF
version: '2.4'

volumes:
    $%service_log_volume%$: {}

services:
  $%service_name%$:
    image: $%service_docker_image_repo%$/$%service_name%$:$%service_version%$
    container_name: $%service_name%$
    network_mode: "host"
    mem_limit : $%service_container_mem_limit%$
    volumes:
      - $%service_log_volume%$:/app/log
    stop_signal: SIGINT
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    entrypoint: java -Xms$%init_heap_size%$m -Xmx$%max_heap_size%$m -Djava.security.egd=file:///dev/urandom -jar /app/$%service_name%$.jar
    command: ["--spring.profiles.active=$%active_profile%$", "--spring.cloud.config.server.git.uri=$%cloud_config_git_uri%$", "--spring.cloud.config.label=$%cloud_config_commit_id%$", "--server.port=$%service_port%$"]
    healthcheck:
      interval: $%health_check_interval%$s
      timeout: $%health_check_timeout%$s
      retries: $%health_check_retry%$
      test: ["CMD", "curl", "-f", "http://localhost:$%service_port%$/actuator/health"]
    restart: always
EOF
else
cat >$%service_name%$.yml <<EOF
version: '2.4'

volumes:
    $%service_log_volume%$: {}

services:
  $%service_name%$:
    image: $%service_docker_image_repo%$/$%service_name%$:$%service_version%$
    container_name: $%service_name%$
    network_mode: "host"
    mem_limit : $%service_container_mem_limit%$
    volumes:
      - $%service_log_volume%$:/app/log
    stop_signal: SIGINT
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    entrypoint: java -Xms$%init_heap_size%$m -Xmx$%max_heap_size%$m -Djava.security.egd=file:///dev/urandom -jar /app/$%service_name%$.jar
    command: ["--spring.cloud.config.uri=$%cloud_config_uri%$", "--spring.profiles.active=$%active_profile%$", "--spring.cloud.config.label=$%cloud_config_commit_id%$", "--server.port=$%service_port%$"]
    healthcheck:
      interval: $%health_check_interval%$s
      timeout: $%health_check_timeout%$s
      retries: $%health_check_retry%$
      test: ["CMD", "curl", "-f", "http://localhost:$%service_port%$/actuator/health"]
    restart: always
EOF
fi

docker-compose -p vsmart -f $%service_name%$.yml up -d

chown -R vsmart:vsmart .

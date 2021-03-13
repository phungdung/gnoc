#!/bin/bash

cd $working_dir

rm fluentd/input/00-vsmart-cloud.conf

cat >fluentd/input/00-vsmart-cloud.conf<<EOF
<source>
    @type tail
    path "/vsmart/*.log"
    format multiline
    format_firstline /\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}.\d{3}/
    format1 /(?<time>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}.\d{3}) (\s*)(?<loglevel>[A-Z]*) (?<pid>[0-9]*) --- \[(?<thread>.*)\] (?<class>[^:]*):(?<line>\d+)([ ]*)(?<traceid>[^: ]*):([ ]*)(?<spanid>[^: ]*) : (?<message>(.|\s)*)\n*/
    pos_file /fluentd/log/vsmart.out.pos
    read_from_head true
    time_key timestamp
    time_format %F %T,%L
    tag cloud.*
</source>
EOF

rm docker-compose.yml

cat >docker-compose.yml<<EOF
version: '2.4'

volumes:
    vsmart_logs: {}
    vsmart_log_pos: {}

networks:
  monitor:

services:
  jaeger-agent:
    image: 10.240.201.50:7890/jaegertracing/jaeger-agent:latest
    command: >
      --collector.host-port=$jaeger_collector_url
    container_name: vsmart-jaeger
    mem_limit: 268435456  # 256MB
    network_mode: "host"
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    restart: always

  node-exporter:
    image: 10.240.201.50:7890/prom/node-exporter:latest
    container_name: vsmart-node-exporter
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    command: >
      --path.procfs=/host/proc
      --path.sysfs=/host/sys
      --collector.filesystem.ignored-mount-points
      ^/(sys|proc|dev|host|etc|rootfs/var/lib/docker/containers|rootfs/var/lib/docker/overlay2|rootfs/run/docker/netns|rootfs/var/lib/docker/aufs)($$|/)
    ports: ['9100:9100']
    mem_limit: 268435456  # 256MB
    networks: [monitor]
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    restart: always

  cadvisor:
    image: 10.240.201.50:7890/google/cadvisor:latest
    container_name: vsmart-cadvisor
    volumes:
      - /:/rootfs:rshared
      - /var/run:/var/run:rshared
      - /sys:/sys:rshared
      - /var/lib/docker/:/var/lib/docker:rshared
      - /cgroup:/cgroup:rshared
    ports: ['8002:8080']
    mem_limit: 268435456  # 256MB
    networks: [monitor]
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    restart: always

  fluentd:
    image: 10.240.201.50:7890/fluentd:latest
    container_name: vsmart-fluentd
    ports: ['24224:24224']
    environment:
      - FLUENT_UID=1001
      - TZ=Asia/Ho_Chi_Minh
    mem_limit: 536870912  # 512MB
    volumes:
      - ./fluentd/:/fluentd/etc/
      - vsmart_logs:/vsmart
      - vsmart_log_pos:/fluentd/log/
    network_mode: "host"
    restart: always

  blackbox-exporter:
    image: 10.240.201.50:7890/prom/blackbox-exporter:latest
    container_name: vsmart-blackbox-exporter
    ports: ['9115:9115']
    mem_limit: 268435456  # 256MB
    command: --config.file=/config/blackbox.yml
    volumes:
      - ./blackbox/:/config
    networks: [monitor]
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    restart: always

  cron:
    image: 10.240.201.50:7890/cron-clean-old-log:latest
    container_name: vsmart-cron-clean-old-log
    mem_limit: 268435456  # 256MB
    volumes:
    - vsmart_logs:/vsmart
    environment:
      TZ: "Asia/Ho_Chi_Minh"
    restart: always

  autoheal:
    image: 10.240.201.50:7890/willfarrell/autoheal:latest
    mem_limit: 67108864  # 64MB
    container_name: vsmart-auto-restart-unhealthy
    environment:
      - AUTOHEAL_CONTAINER_LABEL=all
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    restart: always

EOF

sed -i -e "s/host elasticsearch/host $elastic_search_ip/g" fluentd/output/00-es.conf

docker-compose -p vsmart -f docker-compose.yml up -d

chown -R vsmart:vsmart .

# Ansible playbook for deploy vsmart microservice in production

## Structure

- **utilities** folder: Tools for config servers.
- **heat**: Template for vsmart heat stack.
- **ansible**: Ansible playbooks used to deploy vsmart system.


## How to use utilities

### Bootstrap system

- Modify content of file `sample_config/secret_env_sample.sh` and copy it to `utilities/bootstrap_system` folder as name `secret_env.sh`
- Move to folder `utilities/bootstrap_system`
- Run commands:
 
 ```bash
 
 source secret_env.sh
 bash bootstrap_vsmart.sh

```

### Sample test commands for ansible config servers 

```bash

#!/usr/bin/env bash
cd ansible

ansible -i vsmart_production_static.ini -m ping all

docker push {docker_push_repo_url}/vsmart/production/cdbr-soc-service:1.0
docker push {docker_push_repo_url}/vsmart/production/maintaining-service:1.0
docker push {docker_push_repo_url}/vsmart/production/gnoc-service:1.0
docker push {docker_push_repo_url}/vsmart/production/station-service:1.0
docker push {docker_push_repo_url}/vsmart/production/notification-service:1.0
docker push {docker_push_repo_url}/vsmart/production/nocpro-service:1.0
docker push {docker_push_repo_url}/vsmart/production/gateway-service:1.0
docker push {docker_push_repo_url}/vsmart/production/authentication:1.0
docker push {docker_push_repo_url}/vsmart/production/cdbr-service:1.0
docker push {docker_push_repo_url}/vsmart/production/discovery-service:1.0
docker push {docker_push_repo_url}/vsmart/production/configuration-service:1.0

cd ansible

ansible -i vsmart_production_static.ini -m ping all

# upgrade all services and stack
ansible-playbook -i vsmart_production_static.ini site.yml

# upgrade selected services
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "upgrade-heat-stack,upgrade-configuration-service, upgrade-authentication"

# upgrade selected services without stack
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "upgrade-configuration-service, upgrade-discovery-service"


# create or update stack
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "create-or-update-stack"

# stop stack auto scaling
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "stop-stack-auto-scaling"

# upgrade single service
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "upgrade-configuration-service"

# upgrade single service
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "upgrade-discovery-service"

# upgrade single service
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "upgrade-authentication"

# start stack auto scaling
ansible-playbook -i vsmart_production_static.ini site.yml \
 --tags "start-stack-auto-scaling"

# disable iptables
ansible-playbook -i vsmart_production_static.ini utils.yml  --tags "disable-iptables"  --extra-vars='disable_iptables=true'
# enable iptables
ansible-playbook -i vsmart_production_static.ini utils.yml  --tags "enable-iptables"  --extra-vars='enable_iptables=true'

# fix fluentd config

ansible-playbook -i vsmart_production_static.ini utils.yml  --tags "fix-fluentd-config"

ansible-playbook -i vsmart_production_static.ini utils.yml  --tags "fix-uitls-compose" --extra-vars='enable_utils=true'

ansible-playbook -i vsmart_production_static.ini utils.yml  --tags "stop-system" --extra-vars='enable_utils=true'

#!/usr/bin/env bash
cd ansible

ansible -i vsmart_production_static.ini -m ping all

```

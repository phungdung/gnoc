#!/bin/bash

set -e

cmd=$(basename $0)

sub_help(){
    echo "Usage: $cmd <subcommand> [options]"
    echo "Subcommands:"
    echo "    help              Show help"
    echo "    setup             Setup host"
    echo "    compile           Run compile with docker maven"
    echo "    package           Run package with docker maven"
    echo "    test              Run test with docker maven"
    echo "    coverage          Generate code coverage reports"
    echo "    all-up            Run Docker-compose to start all service in project and manager services"
    echo "    all-down          Run Docker-compose to stop all service in project and manager services"
    echo "    docker-build      Build docker images"
    echo "    docker-up         Run Docker-compose to start services via docker"
    echo "    docker-down       Run Docker-compose to stop services via docker"
    echo "    mgr-up            Run Docker-compose to start manager services via docker"
    echo "    mgr-down          Run Docker-compose to stop manager services via docker"
    echo "    functional_test   Run functional test with docker newman"
    echo ""
    echo "For help with each subcommand run:"
    echo "$cmd <subcommand> -h|--help"
    echo ""
}

sub_getServiceList(){
    groovy GetServerList.groovy $1
}

sub_clean() {
    module=''
    echo "Cleaning for all modules in project"
    _docker_mvn
    echo "DONE!"
}

sub_compile() {
    module=''
    if [ -z $1 ]; then
        echo "Compiling for all modules in project"
    elif [ -n $1 ]; then
        module='-am -pl '$1
        echo "Compiling for module" $1
    fi
    _docker_mvn compile $module -DskipTests
    echo "DONE!"
}

sub_test() {
    module=''
    if [ -z $1 ]; then
        echo "Testing for all modules in project"
    elif [ -n $1 ]; then
        module='-am -pl '$1
        echo "Testing for module" $1
    fi
    _docker_mvn test $module
    echo "DONE!"
}

sub_package() {
    module=''
    if [ -z $1 ]; then
        echo "Packaging for all modules in project"
    elif [ -n $1 ]; then
        module='-am -pl '$1
        echo "Packaging for module" $1
    fi
    _docker_mvn package $module -DskipTests
    echo "DONE!"
}

sub_coverage() {
    echo "Generate code coverage reports for project"
    _docker_mvn test org.jacoco:jacoco-maven-plugin:0.8.3:report-aggregate

    echo "DONE!"
}

sub_docker-build() {
    if [ -d 'commons/target' ]; then
        echo "Already packaged!"
    else
        echo "Need to package before building with docker-compose.."
        sub_package
    fi

    docker-compose -f deploy/docker-compose-services-stack.yml build
}

sub_staging-docker-build() {
    if [ -d 'commons/target' ]; then
        echo "Already packaged!"
    else
        echo "Need to package before building with docker-compose.."
        sub_package
    fi

    eval "$(docker-machine env dev)"

    COMPOSE_HTTP_TIMEOUT=120 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      build
}

sub_service-stack-pull(){
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      pull
}

sub_staging-service-stack-pull(){
    COMPOSE_HTTP_TIMEOUT=120 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      pull
}

sub_service-stack-up() {
    set -e
    # Start configuration service
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      up -d configuration-service

    sub_check-service-health configuration-service

    # Start discovery service
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      up -d discovery-service

    sub_check-service-health discovery-service

    # Start remaining services
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      up -d

}

sub_staging-service-stack-up() {
    set -e
    # Start configuration-service
    COMPOSE_HTTP_TIMEOUT=180 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      up -d configuration-service

    sub_check-service-health configuration-service

    # Start discovery service
    COMPOSE_HTTP_TIMEOUT=120 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      up -d discovery-service

    sub_check-service-health discovery-service

#     Start remaining services
    COMPOSE_HTTP_TIMEOUT=180 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      up -d

}

sub_service-stack-down() {
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      down
}

sub_service-stack-down-rmi() {
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      down --rmi all
}

sub_staging-service-stack-down-rmi() {
    COMPOSE_HTTP_TIMEOUT=120 docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      -f deploy/docker-compose-services-stack-staging.yml \
      down --rmi all
}

sub_stack-show-all() {
    docker-compose -p gnoc \
      -f deploy/docker-compose-services-stack.yml \
      -f deploy/docker-compose-services-stack-dev-remote.yml \
      ps
    docker-compose -p gnoc -f deploy/docker-compose-manager.yml ps
}

sub_check-service-health(){
    set -e
    NEXT_WAIT_TIME=0
    CHECK_CONTAINER=$(echo $1|cut -d'/' -f2)

    CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' ${CHECK_CONTAINER} | tr -d \" )
        while [[ ${CONTAINER_HEALTH_CHECK} != "healthy" ]] && [[ ${NEXT_WAIT_TIME} != 200 ]];  do
            echo "Check container is ${CHECK_CONTAINER} "
            echo "Health status is ${CONTAINER_HEALTH_CHECK}"
            echo "Waits in ${NEXT_WAIT_TIME} s..."
            CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' ${CHECK_CONTAINER} | tr -d \")
            NEXT_WAIT_TIME=$((NEXT_WAIT_TIME+1))
            sleep 1;
        done;

        if  [[ ${CONTAINER_HEALTH_CHECK} != "healthy" ]]; then
          echo "$CHECK_CONTAINER is not healthy"
          exit 1
        else
          echo "$CHECK_CONTAINER is healthy"
        fi

}

sub_service-stack-restart-service(){
    module=''
    if [[ -z $1 ]]; then
        echo "Input service-name is missing. Stop!"
        exit 1
    elif [[ -n $1 ]]; then
        module=$1
    fi

    echo "Restarting $1"
    echo "Stopping $1..."
    docker-compose -p gnoc -f deploy/docker-compose-services-stack.yml -f deploy/docker-compose-services-stack-dev-remote.yml stop $1
    echo "Rebuilding .jar file for $1..."
    _docker_mvn package -Pno-git-commit-id-plugin -pl commons -pl $module -DskipTests
    echo "Rebuilding docker image for $1..."
    docker-compose -p gnoc -f deploy/docker-compose-services-stack.yml -f deploy/docker-compose-services-stack-dev-remote.yml build $1
    echo "Starting $1 container..."
    docker-compose -p gnoc -f deploy/docker-compose-services-stack.yml -f deploy/docker-compose-services-stack-dev-remote.yml up -d $1
    echo "Wait for $1 port is up..."

    NEXT_WAIT_TIME=0
    CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' gnoc-$1 | tr -d \" )

    while [[ ${CONTAINER_HEALTH_CHECK} != "healthy" ]] && [[ ${NEXT_WAIT_TIME} != 200 ]];  do
        echo "Health status is ${CONTAINER_HEALTH_CHECK}"
        echo "Waits in ${NEXT_WAIT_TIME} s..."
        CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' gnoc-$1 | tr -d \")
        ((NEXT_WAIT_TIME++))
        sleep 1;
    done;

    echo "Final Health status of $1 is ${CONTAINER_HEALTH_CHECK}"
    echo "Restart $1 done."
}

sub_mgr-up() {
    COMPOSE_HTTP_TIMEOUT=120 docker-compose -p gnoc -f deploy/docker-compose-manager.yml up -d
    ip=`ifconfig eth0 | sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p'`
    echo "DONE! Access http://$ip for URL list down page"
}

sub_mgr-down() {
    docker-compose -p gnoc -f deploy/docker-compose-manager.yml down -v
}

sub_all-up() {
    sub_mgr-up
    sub_docker-up
}

sub_all-down() {
    sub_docker-down
    sub_mgr-down
}

sub_setup() {
    echo "Setup host for deploying app, require sudo privileges"

    echo "Configuring repo..."
    sudo rm -f /etc/yums.repo.d/*.repo
    sudo cp deploy/docker/local.repo /etc/yum.repos.d/
    sudo yum update -y

    echo "Install packages..."
    sudo yum install -y docker-ce fish vim git telnet python-pip

    echo "Configuring docker..."
    sudo mkdir -p /etc/docker
    sudo cp deploy/docker/daemon.json /etc/docker/
    sudo systemctl daemon-reload
    sudo systemctl enable docker
    sudo systemctl restart docker

    echo "Configuring pip..."
    sudo mkdir -p /root/.config/pip/
    sudo cp deploy/docker/pip.conf /root/.config/pip/

    echo "Install Docker-compose via pip.."
    sudo pip install docker-compose

    echo "Configuring sysctl..."
    sudo cp deploy/docker/sysctl.conf /etc/
    sudo sysctl --load=/etc/sysctl.conf

    echo "Configuring system limits..."
    sudo cp deploy/docker/limits.conf /etc/security/
}

sub_functional_test() {
  echo "Performing functional test"
  _docker_newman $1
}

_docker_mvn() {
    docker run -i --rm \
        -v "$PWD:/usr/src/" \
        -v "$HOME/.m2:/root/.m2" \
        -w /usr/src/ \
        10.240.201.50:7890/maven:oracle-jdk-10 \
        mvn  --settings .settings.xml clean $@
}

_docker_newman() {
    docker run -i --rm \
    -v "$PWD:/usr/src/" \
    -w /usr/src/ \
    --network=host \
    10.240.201.50:7891/node:10.15.1-stretch-slim-v1.1.0 \
    /bin/bash -c "
    node run-collections-in-directory.js \
    --folder=./cdbr-service/src/test/resources/functional_test/ --hostStaging=$1 && \
    node run-collections-in-directory.js \
    --folder=./authz-service/src/test/resources/functional_test/ --hostStaging=$1 && \
    node run-collections-in-directory.js \
    --folder=./gnoc-service/src/test/resources/functional_test/ --hostStaging=$1 && \
    node run-collections-in-directory.js \
    --folder=./hotpot-service/src/test/resources/functional_test/ --hostStaging=$1  &&\
    node run-collections-in-directory.js \
    --folder=./infra_maintaining-service/src/test/resources/functional_test/ --hostStaging=$1 && \
    node run-collections-in-directory.js \
    --folder=./query-service/src/test/resources/functional_test/ --hostStaging=$1"
}

sub_check-folder(){
    ls -la
}

# TODO: Push Docker image to registry?

subcommand=$1

case $subcommand in
    "" | "-h" | "--help")
        sub_help
        ;;
    *)
        shift
        sub_${subcommand} $@
        if [ $? = 127 ]; then
            echo "Error: '$subcommand' is not a known subcommand." >&2
            echo "Run '$cmd --help' for a list of known subcommands." >&2
            exit 1
        fi
        ;;
esac


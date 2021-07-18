#!/bin/bash

mvn clean install -DskipTests

mvn -f ui/pom.xml vaadin:prepare-frontend
mvn -f ui/pom.xml clean package -Pproduction spring-boot:repackage

mvn -f worker/pom.xml clean package spring-boot:repackage
#mvn -f server/pom.xml clean package
mvn -f server/pom.xml package -Pnative -Dquarkus.native.container-build=true

docker build -t sivadocker17/fibonacci-ui ui/.

docker build -t sivadocker17/fibonacci-worker worker/.

#docker build -f server/src/main/docker/Dockerfile.jvm -t sivadocker17/fibonacci-server server/.
docker build -f server/src/main/docker/Dockerfile.native -t sivadocker17/fibonacci-server server/.

docker image ls | head -5

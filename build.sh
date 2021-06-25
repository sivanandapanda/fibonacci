#!/bin/bash

mvn -f client/pom.xml vaadin:prepare-frontend
mvn -f client/pom.xml clean package -Pproduction spring-boot:repackage

mvn -f worker/pom.xml clean package spring-boot:repackage
#mvn -f server/pom.xml clean package
mvn -f server/pom.xml package -Pnative -Dquarkus.native.container-build=true

docker build -t sivadocker17/fibonacci-ui client/.

docker build -t sivadocker17/fibonacci-worker worker/.

#docker build -f server/src/main/docker/Dockerfile.jvm -t sivadocker17/fibonacci-server server/.
docker build -f server/src/main/docker/Dockerfile.native -t sivadocker17/fibonacci-server-native server/.

docker image ls | head -5
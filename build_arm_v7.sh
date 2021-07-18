#!/bin/bash

mvn clean install -DskipTests

mvn -f ui/pom.xml vaadin:prepare-frontend
mvn -f ui/pom.xml clean package -Pproduction spring-boot:repackage

mvn -f worker/pom.xml clean package spring-boot:repackage
mvn -f server/pom.xml clean package

docker build -f ui/Dockerfile.armv7 -t sivadocker17/fibonacci-ui ui/.

docker build -f worker/Dockerfile.armv7 -t sivadocker17/fibonacci-worker worker/.

docker build -f server/src/main/docker/Dockerfile.arkv7 -t sivadocker17/fibonacci-server server/.

docker image ls | head -5

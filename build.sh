#!/bin/bash

mvn -f client/pom.xml vaadin:prepare-frontend
mvn -f client/pom.xml clean package -Pproduction spring-boot:repackage

mvn -f worker/pom.xml clean package spring-boot:repackage
mvn -f server/pom.xml clean package

docker build -t sivadocker17/fibonacci-ui client/.

docker build -t sivadocker17/fibonacci-worker worker/.

docker build -f server/src/main/docker/Dockerfile.jvm -t sivadocker17/fibonacci-server server/.

docker image ls | head -5
#!/bin/bash

mvn -f client/pom.xml vaadin:prepare-frontend
mvn -f client/pom.xml clean package

mvn -f worker/pom.xml clean package
mvn -f server/pom.xml clean package
#!/bin/bash

docker run -d --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgress-db -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:10.5

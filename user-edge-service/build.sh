#!/usr/bin/env bash
mvn clean package
docker build -t 172.27.160.34:8090/micro-service/user-edge-service:latest .
docker push 172.27.160.34:8090/micro-service/user-edge-service:latest
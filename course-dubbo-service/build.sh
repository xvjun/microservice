#!/usr/bin/env bash
mvn clean package
docker build -t 172.27.160.10:8090/micro-service/course-service:latest .
docker push 172.27.160.10:8090/micro-service/course-service:latest
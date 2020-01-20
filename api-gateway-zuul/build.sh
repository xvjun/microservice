#!/usr/bin/env bash
mvn clean package
docker build -t  172.27.160.34:8090/micro-service/api-gateway-zuul:latest .
docker push 172.27.160.34:8090/micro-service/api-gateway-zuul:latest
#docker run -it api-gateway-zuul:latest

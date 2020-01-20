#!/usr/bin/env bash


docker build -t 172.27.160.10:8090/micro-service/python-base:latest -f Dockerfile.base .
docker push 172.27.160.10:8090/micro-service/python-base:latest
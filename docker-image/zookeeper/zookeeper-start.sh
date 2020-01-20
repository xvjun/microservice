#!/usr/bin/env bash
docker stop xujun-zookeeper
docker rm xujun-zookeeper
docker run --name xujun-zookeeper -p 2181:2181git -d zookeeper:3.5

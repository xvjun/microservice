#!/usr/bin/env bash
docker stop xujun-redis
docker rm xujun-redis
docker run -idt -p 6379:6379 -v `pwd`/data:/data --name xujun-redis -v `pwd`/redis.conf:/etc/redis/redis_default.conf redis:latest

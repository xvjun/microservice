#!/bin/bash
HOST_NAME=gitlab.xujun.com
GITLAB_DIR=/data/microservice/gitlab/data
docker stop gitlab
docker rm gitlab
docker run -d \
    --hostname ${HOST_NAME} \
    -p 7443:443 -p 7080:80 -p 2222:22 \
    --name gitlab \
    -v ${GITLAB_DIR}/config:/etc/gitlab \
    -v ${GITLAB_DIR}/logs:/var/log/gitlab \
    -v ${GITLAB_DIR}/data:/var/opt/gitlab \
    registry.cn-hangzhou.aliyuncs.com/imooc/gitlab-ce:latest

#!/usr/bin/env bash

JENKINS_HOME=/Users/xujun/Project/docker_k8s_project/microservice/docker-image/jenkins/data
JAVA_HOME=/usr/bin/java
MAVEN_HOME=/usr/local/apache-maven-3.6.1

docker pull jenkins/jenkins:lts
mkdir -p ${JENKINS_HOME}
#chown -R 1000:1000 ${JENKINS_HOME}
docker stop xujun-jenkins
docker rm xujun-jenkins
docker run \
  --name xujun-jenkins \
  --privileged=true \
  -d \
  -p 9876:8080 \
  -p 50000:50000 \
  -v ${JENKINS_HOME}:/var/jenkins_home \
#  -v ${JAVA_HOME}:${JAVA_HOME} \
  -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):/usr/bin/docker \
#  -v ${MAVEN_HOME}:${MAVEN_HOME} \
  jenkins/jenkins:lts


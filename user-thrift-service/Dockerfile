FROM 172.27.160.34:8090/micro-service/java:8
MAINTAINER xujun 15604288825@163.com

COPY target/user-thrift-service-1.0-SNAPSHOT.jar /user-service.jar

ENTRYPOINT ["java", "-jar", "/user-service.jar"]
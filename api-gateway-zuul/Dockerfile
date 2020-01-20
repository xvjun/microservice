FROM 172.27.160.34:8090/micro-service/java:8
MAINTAINER xujun 15604288825@163.com

COPY target/api-gateway-zuul-1.0-SNAPSHOT.jar /api-gateway-zuul.jar

ENTRYPOINT ["java", "-jar", "/api-gateway-zuul.jar"]
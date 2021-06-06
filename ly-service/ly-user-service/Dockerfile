FROM mcr.microsoft.com/java/jre:8u212-zulu-alpine

MAINTAINER ybx <917367646@qq.com>


EXPOSE 8090

ADD target/ly-user-service-1.0.0-SNAPSHOT.jar ly-user-service-1.0.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","ly-user-service-1.0.0-SNAPSHOT.jar"]

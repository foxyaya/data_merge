FROM mcr.microsoft.com/java/jre:8u212-zulu-alpine

MAINTAINER ybx <917367646@qq.com>


EXPOSE 10010

ADD target/ly-gateway-1.0.0-SNAPSHOT.jar ly-gateway-1.0.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","ly-gateway-1.0.0-SNAPSHOT.jar"]

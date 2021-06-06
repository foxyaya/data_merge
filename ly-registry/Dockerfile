FROM mcr.microsoft.com/java/jre:8u212-zulu-alpine

MAINTAINER ybx <917367646@qq.com>


EXPOSE 10086

ADD target/leyou-registry-1.0.0-SNAPSHOT.jar leyou-registry-1.0.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","leyou-registry-1.0.0-SNAPSHOT.jar"]

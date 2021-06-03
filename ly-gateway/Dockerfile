FROM mcr.microsoft.com/java/jre:8u212-zulu-alpine

MAINTAINER ybx <917367646@qq.com>


EXPOSE 9091

ADD target/demo-1.0.0-SNAPSHOT.jar demo-1.0.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","demo-1.0.0-SNAPSHOT.jar"]

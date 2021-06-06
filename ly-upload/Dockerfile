FROM mcr.microsoft.com/java/jre:8u212-zulu-alpine

MAINTAINER ybx <917367646@qq.com>


EXPOSE 8082

ADD target/ly-upload-1.0.0-SNAPSHOT.jar ly-upload-1.0.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","ly-upload-1.0.0-SNAPSHOT.jar"]

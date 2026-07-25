#FROM openjdk:8-jdk-alpine
FROM your-registry.cn-region.personal.cr.aliyuncs.com/your-namespace/java:8-jdk-alpine
#RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
#RUN echo "Asia/Shanghai" > /etc/timezone
# 设置时区和语言
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US.UTF-8
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /
COPY ./target/duanju-server.jar app.jar
EXPOSE 8183

#CMD java -jar app.jar
ENTRYPOINT ["java","-Xms2g","-Xmx2g","-Djava.security.egd=file:/dev/./urandom","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8017","/app.jar"]
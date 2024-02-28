FROM openjdk:17
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY target/catman-coder-workbench-api-http.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
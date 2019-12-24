#编译
FROM maven:3.6.3-jdk-8 as builder
WORKDIR /itxia
COPY . .
RUN mvn install -Dmaven.test.skip=true

#运行
FROM openjdk:8
WORKDIR /itxia
COPY --from=builder /itxia/target/back-end-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
CMD java -jar app.jar
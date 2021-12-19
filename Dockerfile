FROM mcr.microsoft.com/openjdk/jdk:11-mariner

ADD target/hellospring-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]

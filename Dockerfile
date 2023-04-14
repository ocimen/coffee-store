FROM  openjdk:17-oracle

EXPOSE 8080

COPY target/coffee-store*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
FROM openjdk:8-alpine

COPY target/uberjar/gallery-lumin.jar /gallery-lumin/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/gallery-lumin/app.jar"]

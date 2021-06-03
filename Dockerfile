FROM openjdk:8
ADD target/egasvc-mysql.jar egasvc-mysql.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "egasvc-mysql.jar", "-Dspring.profiles.active=dev"]
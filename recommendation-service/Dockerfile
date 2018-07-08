FROM java:8-jre
MAINTAINER Leonardo Nascimento <leomn138@gmail.com>

ADD ./target/recommendation-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/recommendation-service.jar"]

EXPOSE 6000
FROM java:8-jre
MAINTAINER Thien Tran <thientran1986@gmail.com>


ADD ./target/codelab-ssh-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/codelab-ssh-service.jar"]

EXPOSE 20081

FROM java:8-jre-alpine

COPY ./binaries/flex-api.jar /opt/flex-api/flex-api.jar

WORKDIR /opt/flex-api/

RUN sh -c 'touch flex-api.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -server $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar flex-api.jar \
    --spring.config.location=classpath:/application.yml --logging.file=/var/log/flex/flex-api.log" ]

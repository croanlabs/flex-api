FROM java:8-jre-alpine

COPY ./flex-api.jar /opt/flex-api/flex-api.jar

WORKDIR /opt/flex-api/

#Set profile the application is in, dev,test or prod. Default is prod
ARG SPRING_PROFILE=prod

RUN echo "Application profile set to \"$SPRING_PROFILE\""
RUN sh -c 'touch flex-api.jar'
ENV JAVA_OPTS="-Dspring.profiles.active=${SPRING_PROFILE}"
ENTRYPOINT [ "sh", "-c", "java -server $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar flex-api.jar \
    --spring.config.location=classpath:/application.yml --logging.file=/var/log/flex/flex-api.log" ]

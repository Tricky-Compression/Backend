FROM gradle:7.4.1-jdk17-alpine AS BUILD
ARG APP_HOME=/app
WORKDIR $APP_HOME
RUN chown -R gradle:gradle $APP_HOME/
COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src/ $APP_HOME/src/
RUN gradle --quiet assemble --no-daemon --no-build-cache --parallel

FROM openjdk:17-alpine
ARG APP_HOME=/app
ENV ARTIFACT_NAME=Backend-1.0-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/$ARTIFACT_NAME .
EXPOSE 8080
ENTRYPOINT java -jar $ARTIFACT_NAME

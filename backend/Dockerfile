FROM gradle:7.4-jdk17 AS build
COPY . /app
WORKDIR /app
RUN gradle build


FROM eclipse-temurin:17-jre-alpine


RUN apk add --no-cache tzdata
RUN cp /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime
RUN echo "Europe/Amsterdam" > /etc/timezone

COPY --from=build /app/build/libs/rijksoverheid-0.0.1-SNAPSHOT.jar /rijksoverheid-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/rijksoverheid-0.0.1-SNAPSHOT.jar"]
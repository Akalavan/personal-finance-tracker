FROM openjdk:21-slim as build

WORKDIR /build

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract --destination extracted


FROM openjdk:21-slim

RUN apt-get update && apt-get install -y --no-install-recommends adduser curl && \
        adduser --system config && \
        addgroup --system config && \
        usermod -aG config config && \
        apt-get clean && rm -rf /var/lib/apt/lists/*

USER config

WORKDIR /application

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}

# Etapa 1: Build
FROM alpine/java:21 AS builder

WORKDIR /build

COPY . .

RUN chmod +x gradlew && \
    ./gradlew bootJar --no-daemon

# Etapa 2: Runtime
FROM alpine/java:21

WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

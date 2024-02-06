FROM eclipse-temurin:21-jdk as build
COPY . /app
WORKDIR /app
RUN ./mvnw package -DskipTests
RUN mv -f target/*.jar app.jar

FROM eclipse-temurin:21-jre
ARG DATABASE_URL
ARG DATABASE_USERNAME
ARG DATABASE_PASSWORD
ENV DATABASE_URL=${DATABASE_URL}
ENV DATABASE_USERNAME=${DATABASE_USERNAME}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}
COPY --from=build /app/app.jar .
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-jar", "app.jar" ]
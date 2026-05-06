FROM node:22-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d7t3qm0k1i2s73aakr10-a:5432/toyshop_hxxt
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=N8ogDTpsyM8bRQKLTqX6b1cULOGICOKC
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=${SPRING_DATASOURCE_URL}", "--spring.datasource.username=${SPRING_DATASOURCE_USERNAME}", "--spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"]
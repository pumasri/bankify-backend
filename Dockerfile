# =========================
# Build stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app





# Copy only pom.xml first (better cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests


# =========================
# Run stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","/app/app.jar"]

# Stage 1: Build the application using official Java 25
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# 🚨 THE FIX: Install curl first, since the naked base image doesn't have it!
RUN apt-get update && apt-get install -y curl

# Safely download and install Maven directly from Apache
RUN curl -sL https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz | tar xz -C /opt && \
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn

# Copy your code
COPY pom.xml .
COPY src ./src

# Limit RAM to prevent silent crashes, and use -B to stop log flooding
ENV MAVEN_OPTS="-Xmx256m"
RUN mvn clean package -DskipTests -B -e

# Stage 2: Run the application
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
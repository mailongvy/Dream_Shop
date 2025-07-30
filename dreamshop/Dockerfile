FROM openjdk:24-jdk-slim

# Set working directory
WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper và pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Set execute permission for mvnw
RUN chmod +x ./mvnw

# Download dependencies (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Change ownership of the JAR file
RUN chown spring:spring target/DreamShop-0.0.1-SNAPSHOT.jar

USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with correct JAR name
ENTRYPOINT ["java", "-jar", "target/DreamShop-0.0.1-SNAPSHOT.jar"]
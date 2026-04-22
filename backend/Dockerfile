# ============================
# WMS-AI Backend — Multi-stage Build
# ============================

# --- Stage 1: Build ---
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and POM first (layer caching for dependencies)
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Make mvnw executable and download dependencies
RUN chmod +x mvnw && ./mvnw dependency:resolve -B -q

# Copy source code and build
COPY src src
RUN ./mvnw package -DskipTests -B -q

# --- Stage 2: Runtime ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -r wmsai && useradd -r -g wmsai -d /app -s /sbin/nologin wmsai

# Create data directories
RUN mkdir -p /app/wms-data/logs && chown -R wmsai:wmsai /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar
RUN chown wmsai:wmsai app.jar

# Switch to non-root user
USER wmsai

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -s http://localhost:8080/api/auth/login >/dev/null || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar", \
    "--spring.profiles.active=docker"]

# ============================================================
# Stage 1: Build - Compilar la aplicación con Maven
# ============================================================
FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar solo archivos de dependencias primero (para caché)
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar (skip tests para velocidad)
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Production - Imagen final ligera
# ============================================================
FROM eclipse-temurin:21-jre-alpine

# Labels de metadata
LABEL maintainer="TalentoTech" \
      version="1.0" \
      description="Backend API - TechLab E-commerce"

# Crear usuario no-root para seguridad
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Directorio de trabajo
WORKDIR /app

# Cambiar ownership de los archivos
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Cambiar a usuario no-root
RUN chown -R appuser:appgroup /app
USER appuser

# Puerto expuesto
EXPOSE 8080

# Configuración de memoria JVM optimizada
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Arranque de la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# ============================================================
#-build.sh (opcional - para build local):
#docker build -t talentotech-api .
#docker run -p 8080:8080 -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=... -e JWT_SECRET=... talentotech-api
# ============================================================
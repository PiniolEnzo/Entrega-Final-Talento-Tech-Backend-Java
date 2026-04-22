# ---------- Etapa 1: build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiamos primero el pom para aprovechar cache
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto
RUN mvn -B -DskipTests package

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiamos el jar generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app/talentotech.jar

EXPOSE 3000

ENTRYPOINT ["java","-jar","app/talentotech.jar"]
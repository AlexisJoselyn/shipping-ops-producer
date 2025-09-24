# ---------- Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./

# Descargar dependencias para cache eficiente
RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -e -DskipTests package

# ---------- Runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /app/target/*.jar app.jar

# Variable opcional para pasar flags a la JVM
ENV JAVA_OPTS=""

# Puerto (se ajusta según el microservicio)
EXPOSE 8087

# Se agrega una espera activa para asegurar que Redis esté listo
RUN apt-get update && apt-get install -y wait-for-it
ENTRYPOINT ["sh", "-c", "wait-for-it redis:6379 --timeout=30 --strict -- java $JAVA_OPTS -jar app.jar"]
# ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

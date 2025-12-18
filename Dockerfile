# Etapa de build
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

#se agregar una variable de entorno por si el docker no lee bien
ENV LANG en_US.UTF-8

# Copia cóoigo fuente todo, incluyendo pom.xml y src
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre

# Define el directorio de trabajo donde estará la app.jar
WORKDIR /app

# **CORRECCIÓN 1: Copiar el JAR con su nombre exacto**
# Copia el JAR ejecutable desde la etapa 'build' y lo renombra a 'app.jar' en el runtime.
# Usa el nombre EXACTO de tu JAR:
COPY --from=build /app/target/franquicias-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# **CORRECCIÓN 2: Corregir el ENTRYPOINT**
# Ya que WORKDIR es /app, solo necesitamos 'app.jar' sin la barra inicial.
ENTRYPOINT ["java", "-jar", "app.jar"]
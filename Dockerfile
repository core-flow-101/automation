# Этап сборки
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Копируем файлы конфигурации Gradle
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Копируем исходный код
COPY src src

# Собираем приложение
RUN gradle build -x test --no-daemon

# Финальный этап
FROM eclipse-temurin:17-jre

WORKDIR /app

# Копируем собранный JAR из этапа сборки
COPY --from=build /app/build/libs/spring-boot-app-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]


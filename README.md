# Spring Boot Application

Приложение на базе Spring Boot с использованием Spring Data JPA и PostgreSQL.

## Технологии

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Gradle
- Docker Compose

## Запуск приложения

### Вариант 1: Запуск через Docker Compose (рекомендуется)

Запускает PostgreSQL и приложение вместе:

```bash
docker compose up -d
```

Или для пересборки образа:

```bash
docker compose up -d --build
```

Приложение будет доступно по адресу: http://localhost:8080

### Вариант 2: Локальный запуск

#### 1. Запуск PostgreSQL через Docker Compose

```bash
docker compose up -d postgres
```

Это поднимет PostgreSQL контейнер на порту 5432.

#### 2. Сборка проекта

```bash
./gradlew build
```

На Windows:
```bash
gradlew.bat build
```

#### 3. Запуск приложения

```bash
./gradlew bootRun
```

На Windows:
```bash
gradlew.bat bootRun
```

Или через скомпилированный JAR:

```bash
java -jar build/libs/spring-boot-app-0.0.1-SNAPSHOT.jar
```

Приложение будет доступно по адресу: http://localhost:8080

## Остановка приложения

Остановка всех сервисов:

```bash
docker compose down
```

Для удаления данных (volumes):

```bash
docker compose down -v
```

## Настройка базы данных

Параметры подключения к БД находятся в `src/main/resources/application.yml`:

- URL: `jdbc:postgresql://localhost:5432/mydb`
- Username: `postgres`
- Password: `postgres`
- Port: `5432`

## API Endpoints

### Создание студента

**POST** `/api/students`

Тело запроса:
```json
{
  "firstName": "Иван",
  "lastName": "Иванов",
  "studyGroup": "ИТ-21"
}
```

Ответ: `201 Created` с ID студента в теле ответа (например: `1`)

### Получение студента по ID

**GET** `/api/students/{id}`

Ответ: `200 OK` с данными студента:
```json
{
  "id": 1,
  "firstName": "Иван",
  "lastName": "Иванов",
  "studyGroup": "ИТ-21"
}
```

Или `404 Not Found`, если студент не найден.

### Actuator Endpoints

- **GET** `/actuator/health` - проверка здоровья приложения
- **GET** `/actuator/info` - информация о приложении
- **GET** `/actuator/metrics` - метрики приложения

## Структура проекта

```
src/
├── main/
│   ├── java/
│   │   └── com/example/application/
│   │       ├── Application.java
│   │       ├── controller/
│   │       │   └── StudentController.java
│   │       ├── entity/
│   │       │   └── Student.java
│   │       ├── repository/
│   │       │   └── StudentRepository.java
│   │       └── dto/
│   │           ├── StudentRequest.java
│   │           └── StudentResponse.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/
```


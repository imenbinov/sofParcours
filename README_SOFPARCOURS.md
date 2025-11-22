# SofParcours - API Backend Quiz & IA

## Description

**SofParcours** est une API REST Spring Boot (Java 11) permettant de créer et gérer des quiz en temps réel avec des salles de jeu, un système de badges/points, une intégration IA (GPT) et des fonctionnalités Green IT.

## Technologies

- Java 11
- Spring Boot 2.7.18
- MongoDB
- Springdoc OpenAPI (Swagger UI)
- JWT (authentification)
- Spring Security (BCrypt)
- Lombok

## Demarrage rapide

### Prerequis

- Java 11 installe
- MongoDB en cours d'execution sur localhost:27017
- Maven (fourni via mvnw)

### Configuration application.properties

```properties
spring.application.name=SofQuizRoom
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/sofquizroom

springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html

spring.security.jwt.secret=ChangeThisSecretInProd
spring.security.jwt.expiration=3600000

ai.api.key=REMPLACE_MOI_AVEC_TA_CLE
ai.api.url=https://api.openai.com/v1/gpt-5/chat/completions
ai.api.timeout-ms=10000
```

### Dependances a ajouter dans pom.xml

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Lancer l'application

```powershell
./mvnw clean package
./mvnw spring-boot:run
```

### URLs

- http://localhost:8080 - Page d'accueil
- http://localhost:8080/swagger-ui.html - Documentation Swagger
- http://localhost:8080/api/ai - Endpoint IA
- http://localhost:8080/api/auth/register - Inscription
- http://localhost:8080/api/auth/login - Connexion
- http://localhost:8080/api/rooms - Gestion rooms

## Endpoints disponibles

### Authentification /api/auth

**POST /api/auth/register** - Inscription

Body:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**POST /api/auth/login** - Connexion

Body:
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

### Rooms /api/rooms

**POST /api/rooms** - Creer une room

Body:
```json
{
  "name": "Quiz Java Basics",
  "organizerId": "507f1f77bcf86cd799439011"
}
```

**POST /api/rooms/{code}/join** - Rejoindre une room

Body:
```json
{
  "userId": "507f1f77bcf86cd799439012"
}
```

**GET /api/rooms/{code}** - Details d'une room

### IA /api/ai

**POST /api/ai** - Envoyer un prompt a l'IA

Body:
```json
{
  "prompt": "Explique-moi Spring Boot en une phrase."
}
```

## Structure du projet

```
src/main/java/com/hackathon/sofParcours/
├── SofParcoursApplication.java
├── config/SwaggerConfig.java
├── controller/
│   ├── AIController.java
│   ├── AuthController.java
│   ├── RoomController.java
│   ├── HomeController.java
│   └── dto/
├── model/
│   ├── User.java
│   ├── Badge.java
│   ├── Room.java
│   └── Participant.java
├── repository/
│   ├── UserRepository.java
│   ├── RoomRepository.java
│   └── BadgeRepository.java
└── service/
    ├── AIService.java
    ├── AuthService.java
    └── RoomService.java
```

## Fonctionnalites implementees

### Phase 1 : Auth & Rooms - COMPLETE
- Inscription/Connexion utilisateur (JWT)
- Creation de room avec code unique
- Rejoindre une room
- Affichage des participants

### Integration IA - COMPLETE
- Endpoint /api/ai pour prompts
- Configuration cle API
- Gestion d'erreurs

### Phase 2-6 : A IMPLEMENTER
- Quiz temps reel
- Badges & Points
- Profils & Historique
- Feedback & Partage
- Green IT optimisations

## Tests

```powershell
./mvnw test
```

## Build Production

```powershell
./mvnw clean package
java -jar target/sofParcours-0.0.1-SNAPSHOT.jar
```

## Prochaines etapes

1. Ajouter JWT + Spring Security dans pom.xml
2. Rebuild le projet
3. Implementer Phase 2 : Quiz temps reel
4. Implementer Phase 3 : Badges & Points
5. Ajouter cache IA

Bon developpement avec SofParcours!

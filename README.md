# SofParcours - Quiz Platform avec Génération IA

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Description

**SofParcours** est une plateforme de quiz interactive avec génération automatique de contenu par IA. L'application permet de créer des salles de quiz (Rooms) avec questions générées intelligemment via OpenAI GPT, un système de badges, de scoring et de leaderboard en temps réel.

## 🚀 Fonctionnalités

### ✅ Implémentées
- **Génération IA de Quiz** : Endpoint idempotent `/api/rooms/search-or-create` qui génère automatiquement Room + Quiz + Questions
- **Gestion des Rooms** : Création, jointure, statut des salles de quiz
- **Système de Quiz** : Questions à choix multiples avec scoring
- **Authentification** : JWT + Spring Security
- **Badges & Scoring** : Système de points et récompenses
- **Leaderboard** : Classement des joueurs
- **API Documentation** : Swagger UI intégré
- **Normalisation Slug** : Système idempotent pour éviter les doublons

### 🔄 Architecture

```
┌─────────────┐      ┌──────────────────┐      ┌─────────────┐
│   Client    │─────▶│  RoomController  │─────▶│  MongoDB    │
└─────────────┘      └──────────────────┘      └─────────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │RoomCreationService│
                     └──────────────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
            ┌───────────────┐   ┌──────────────┐
            │  AIService    │   │ RoomRepository│
            │  (OpenAI GPT) │   │  findBySlug() │
            └───────────────┘   └──────────────┘
```

## 🛠️ Technologies

- **Backend** : Spring Boot 3.2, Java 21
- **Database** : MongoDB
- **AI** : OpenAI GPT-3.5/4
- **Security** : Spring Security, JWT
- **API Doc** : Springdoc OpenAPI (Swagger)
- **Cache** : Spring Cache
- **Build** : Maven

## 📦 Installation

### Prérequis

- Java 21 ou supérieur
- MongoDB (localhost:27017 ou Atlas)
- Maven 3.8+
- Clé API OpenAI

### Configuration

**1. Cloner le projet**
```bash
git clone https://github.com/imenbinov/sofParcours.git
cd sofParcours
```

**2. Configurer `application.properties`**
```properties
# Application
spring.application.name=SofQuizRoom
server.port=8080

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sofparcours
spring.data.mongodb.database=sofparcours

# AI Service (IMPORTANT: Remplacer par votre vraie clé)
ai.api.key=sk-votre-cle-openai
ai.api.url=https://api.openai.com/v1/chat/completions
ai.api.timeout-ms=10000

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# CORS
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
```

**3. Lancer l'application**
```bash
mvn clean install
mvn spring-boot:run
```

**4. Accéder à l'application**
- Application : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html
- API Docs : http://localhost:8080/api-docs

## 📚 API Endpoints

### 🤖 Génération IA de Quiz

**GET /api/rooms/search-or-create**

Endpoint idempotent qui recherche ou crée automatiquement une Room avec Quiz et Questions générés par IA.

**Paramètres:**
- `q` (required) : Sujet du quiz (ex: "DevOps avancé")
- `userProfile` (optional) : Profil utilisateur (défaut: "anonymous")

**Exemple:**
```bash
curl "http://localhost:8080/api/rooms/search-or-create?q=DevOps%20avancé"
```

**Réponse:**
```json
{
  "id": "65f1a2b3c4d5e6f7g8h9i0j1",
  "name": "DevOps Avancé",
  "description": "Quiz complet sur les pratiques DevOps avancées",
  "code": "ABC123",
  "slug": "devops-avance",
  "generatedByAI": true,
  "generatedAt": "2024-01-20T10:30:00",
  "quiz": {
    "title": "DevOps Avancé - Quiz",
    "questions": [
      {
        "text": "Qu'est-ce que le CI/CD?",
        "type": "multiple_choice",
        "options": ["..."],
        "correctAnswer": "...",
        "explanation": "...",
        "points": 10
      }
    ]
  }
}
```

### 🏠 Rooms

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/rooms` | Liste toutes les rooms |
| GET | `/api/rooms/{id}` | Détails d'une room |
| GET | `/api/rooms/code/{code}` | Room par code |
| POST | `/api/rooms` | Créer une room |
| POST | `/api/rooms/{code}/join` | Rejoindre une room |
| PUT | `/api/rooms/{id}/status` | Modifier statut |

### 📝 Quiz

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/quiz/generate` | Générer questions via IA |
| GET | `/api/quiz/{id}` | Détails d'un quiz |
| POST | `/api/quiz/{id}/submit` | Soumettre une réponse |

### 🏆 Leaderboard

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/leaderboard/global` | Classement global |
| GET | `/api/leaderboard/room/{code}` | Classement par room |

### 🔐 Auth

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |

## 🌐 Exposer l'API avec ngrok

Pour partager votre API avec des collaborateurs:

```bash
# 1. Démarrer l'application
mvn spring-boot:run

# 2. Dans un autre terminal, lancer ngrok
ngrok http 8080

# 3. Partager l'URL générée
https://abc123.ngrok-free.app/swagger-ui.html
```

## 📁 Structure du Projet

```
src/main/java/com/hackathon/sofParcours/
├── SofParcoursApplication.java
├── config/
│   ├── SwaggerConfig.java
│   ├── CacheConfig.java
│   └── DataInitializer.java
├── controller/
│   ├── RoomController.java          # ✨ Endpoint search-or-create
│   ├── QuizController.java
│   ├── AuthController.java
│   ├── LeaderboardController.java
│   └── BadgeController.java
├── service/
│   ├── RoomCreationService.java     # ✨ Logique search-or-create
│   ├── AIService.java               # ✨ Intégration OpenAI
│   ├── RoomService.java
│   ├── QuizService.java
│   └── ScoringService.java
├── model/
│   ├── Room.java                    # ✨ + slug, generatedByAI
│   ├── Quiz.java
│   ├── Question.java
│   ├── User.java
│   └── Badge.java
├── repository/
│   ├── RoomRepository.java          # ✨ + findBySlug()
│   ├── QuizRepository.java
│   ├── QuestionRepository.java
│   └── UserRepository.java
└── dto/
    ├── AIRoomResponse.java          # ✨ Nouveau
    ├── RoomDTO.java                 # ✨ Nouveau
    ├── QuizDTO.java                 # ✨ Nouveau
    └── QuestionDTO.java             # ✨ Nouveau
```

## 🧪 Tests

```bash
# Tests unitaires
mvn test

# Tests avec couverture
mvn test jacoco:report
```

## 🚢 Déploiement

### Build Production

```bash
mvn clean package -DskipTests
java -jar target/sofParcours-0.0.1-SNAPSHOT.jar
```

### Docker (optionnel)

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/sofParcours-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t sofparcours .
docker run -p 8080:8080 sofparcours
```

## 📖 Documentation Complète

Consultez [API_DOCUMENTATION.md](API_DOCUMENTATION.md) pour la documentation détaillée de l'endpoint `/search-or-create`.

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 Roadmap

- [x] Génération automatique de quiz via IA
- [x] Système de rooms avec code unique
- [x] Badges et scoring
- [x] Leaderboard
- [ ] WebSocket pour temps réel
- [ ] Analytics et statistiques
- [ ] Export des résultats (PDF/CSV)
- [ ] Multi-langue (i18n)

## 👥 Équipe

- **Développeurs Backend** : Équipe Hackathon SofParcours
- **Repository** : [github.com/imenbinov/sofParcours](https://github.com/imenbinov/sofParcours)

## 📄 License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

**🎯 Happy Coding with SofParcours!** 🚀

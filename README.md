# 🎯 SofParcours - Quiz Application avec IA

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📝 Description

SofParcours est une application de quiz interactive avec génération intelligente de questions par IA. L'application permet de créer des salles de quiz en temps réel avec système de badges, scores et classements.

## ✨ Fonctionnalités

- 🤖 **Génération de questions par IA** - Questions générées automatiquement et sauvegardées
- 🏠 **Gestion de Rooms** - Créez et rejoignez des salles de quiz
- 🎮 **Quiz en temps réel** - Workflow dynamique et interactif
- 🏆 **Système de Badges** - Débloquez des badges selon vos performances
- 📊 **Scoring & Classements** - Points basés sur le temps de réponse
- 📚 **Cache intelligent** - Questions sauvegardées en MongoDB
- 🔄 **RESTful API** - Endpoints complets et documentés
- 📖 **Documentation Swagger** - Interface interactive pour tester l'API

## 🚀 Technologies

- **Backend:** Java 21, Spring Boot 3.2.1
- **Base de données:** MongoDB
- **Documentation:** Springdoc OpenAPI (Swagger)
- **Architecture:** REST API

## 📋 Prérequis

- Java 21 ou supérieur
- Maven 3.8+
- MongoDB 7.0+ (local ou cloud)

## ⚙️ Installation

### 1. Cloner le repository
```bash
git clone https://github.com/imenbinov/sofParcours.git
cd sofParcours
```

### 2. Configurer MongoDB
Assurez-vous que MongoDB est en cours d'exécution sur `localhost:27017`

Ou modifiez `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/sofparcours
```

### 3. Compiler et lancer l'application
```bash
mvn clean install
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8080**

## 📚 Documentation API

Une fois l'application lancée, accédez à la documentation Swagger:

**http://localhost:8080/swagger-ui.html**

## 🎮 Workflow Principal

### 1. Créer une Room
```bash
POST /api/rooms
{
  "name": "Quiz Java",
  "description": "Test IA",
  "createdBy": "admin"
}
```

### 2. Créer un Quiz
```bash
POST /api/quizzes
{
  "title": "Quiz Spring Boot",
  "roomCode": "ABC123",
  "topic": "Spring Boot",
  "difficulty": "MEDIUM"
}
```

### 3. Générer des Questions avec l'IA
```bash
GET /api/quizzes/{quizId}/questions?numberOfQuestions=5
```

**🎉 Les questions sont générées par l'IA, affichées ET sauvegardées !**

### 4. Soumettre une Réponse
```bash
POST /api/quizzes/answers
{
  "userId": "user1",
  "questionId": "q1",
  "quizId": "quiz123",
  "selectedOptionIndex": 0,
  "responseTimeMs": 15000
}
```

## 🏗️ Structure du Projet

```
src/main/java/com/hackathon/sofParcours/
├── config/          # Configuration (DataInitializer, CORS, etc.)
├── controller/      # REST Controllers
├── dto/             # Data Transfer Objects
├── model/           # Entités MongoDB
├── repository/      # Repositories MongoDB
└── service/         # Logique métier (AI, Quiz, Scoring, etc.)
```

## 🎯 Endpoints Principaux

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/rooms` | Liste toutes les rooms |
| POST | `/api/rooms` | Créer une room |
| GET | `/api/quizzes/room/{code}` | Quiz d'une room |
| POST | `/api/quizzes` | Créer un quiz |
| GET | `/api/quizzes/{id}/questions` | Récupérer/Générer questions IA |
| POST | `/api/quizzes/answers` | Soumettre une réponse |
| GET | `/api/quizzes/{id}/results` | Résultats du quiz |

## 🎨 Données Initiales

Au premier démarrage, l'application initialise automatiquement:
- 5 Badges
- 4 Rooms de démonstration
- 2 Utilisateurs de test

Voir `DATA_INITIALIZATION.md` pour plus de détails.

## 🔧 Configuration

Fichier: `src/main/resources/application.properties`

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sofparcours

# Port serveur
server.port=8080

# IA (mode démo par défaut)
ai.api.key=demo-key
ai.api.url=https://api.openai.com/v1/chat/completions
```

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer:

1. Fork le projet
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 License

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👥 Auteurs

- **Imen Binov** - [imenbinov](https://github.com/imenbinov)

## 🙏 Remerciements

- Spring Boot Team
- MongoDB
- OpenAPI/Swagger

---

⭐ Si ce projet vous plaît, n'hésitez pas à lui donner une étoile !

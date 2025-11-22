# 🎯 SofParcours - Plateforme de Quiz avec IA

Une application Spring Boot complète de quiz collaboratif avec intégration GPT-5, système de badges, et optimisations Green IT.

---

## 📋 Table des Matières

- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Architecture](#-architecture)
- [API Endpoints](#-api-endpoints)
- [Fonctionnalités](#-fonctionnalités)
- [Optimisations Green IT](#-optimisations-green-it)
- [Configuration](#-configuration)

---

## 🔧 Prérequis

- **Java 11+**
- **Maven 3.6+**
- **MongoDB 4.4+** (local ou cloud)
- **Clé API OpenAI** (pour l'intégration GPT-5)

---

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone <votre-repo>
cd sofParcours
```

### 2. Configurer MongoDB

Démarrez MongoDB localement :

```bash
mongod --dbpath /data/db
```

Ou utilisez MongoDB Atlas (cloud) et modifiez `application.properties`.

### 3. Ajouter les dépendances manquantes au pom.xml

**JWT & Spring Security** :

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

### 4. Configurer les clés API

Éditez `src/main/resources/application.properties` :

```properties
# Clé API OpenAI (OBLIGATOIRE pour l'IA)
ai.api.key=your_openai_api_key_here

# JWT Secret (changez en production)
jwt.secret=your_super_secret_jwt_key_change_this_in_production
```

### 5. Compiler et lancer

```bash
./mvnw clean package
./mvnw spring-boot:run
```

L'application démarre sur **http://localhost:8080**

---

## 🏗️ Architecture

### **Modèles de données**

| Modèle | Description |
|--------|-------------|
| `User` | Utilisateur (username, email, password hashé, badges, score total) |
| `Room` | Salle de quiz (code 6 chiffres, participants, statut) |
| `Quiz` | Quiz associé à une room (questions, statut, timestamps) |
| `Question` | Question avec options et réponse correcte |
| `Answer` | Réponse soumise par un utilisateur (points, temps de réponse) |
| `QuizHistory` | Historique des quiz complétés |
| `Badge` | Badge à débloquer (nom, description, points requis) |
| `Comment` | Commentaire sur une question |

### **Services principaux**

- **AuthService** : Authentification JWT + BCrypt
- **RoomService** : Gestion des rooms (création, join)
- **QuizService** : Gestion des quiz (création, démarrage, soumission)
- **ScoringService** : Calcul des scores et classements
- **BadgeService** : Attribution automatique de badges
- **AIService** : Intégration GPT-5 avec cache

---

## 🌐 API Endpoints

### **Authentication** (`/api/auth`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/register` | Créer un compte (retourne JWT) |
| POST | `/login` | Se connecter (retourne JWT) |

**Exemple Register** :
```json
POST /api/auth/register
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "securepass123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMi...",
  "username": "alice"
}
```

---

### **Rooms** (`/api/rooms`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/` | Créer une room (retourne code 6 chiffres) |
| POST | `/{code}/join` | Rejoindre une room |
| GET | `/{code}` | Obtenir les détails d'une room |

**Exemple Create Room** :
```json
POST /api/rooms
{
  "organizerId": "user123",
  "organizerName": "Alice"
}

Response:
{
  "id": "abc123",
  "code": "483726",
  "status": "WAITING",
  "participants": [
    {"userId": "user123", "username": "Alice", "score": 0}
  ]
}
```

---

### **Quiz** (`/api/quiz`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/` | Créer un quiz |
| POST | `/{quizId}/start` | Démarrer un quiz |
| POST | `/{quizId}/answer` | Soumettre une réponse |
| GET | `/{quizId}/leaderboard` | Classement du quiz |
| GET | `/{quizId}` | Détails du quiz |
| POST | `/questions` | Créer une question |
| GET | `/questions` | Lister toutes les questions |

**Exemple Submit Answer** :
```json
POST /api/quiz/quiz123/answer
{
  "questionId": "q456",
  "userId": "user789",
  "selectedOptionIndex": 2,
  "responseTimeMs": 3500
}

Response:
{
  "id": "ans999",
  "correct": true,
  "pointsEarned": 125,  // Points de base + bonus rapidité
  "responseTimeMs": 3500,
  "answeredAt": "2025-11-22T14:30:00"
}
```

**Formule de score** :
```
Points = PointsDeBase + (PointsDeBase × 0.5 × (1 - TempsRéponse/TempsLimite))
Bonus max : +50% pour réponse instantanée
```

---

### **Badges** (`/api/badges`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/` | Lister tous les badges disponibles |
| GET | `/user/{userId}` | Badges obtenus par un utilisateur |

**Badges disponibles** :
- 🏆 **Première victoire** : Compléter son premier quiz
- 🎓 **Expert** : 10 bonnes réponses d'affilée
- 👑 **Champion** : Score total ≥ 500

---

### **Profil** (`/api/profile`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/{userId}` | Profil complet (badges, scores, stats) |
| GET | `/{userId}/history` | Historique des quiz |
| GET | `/{userId}/badges` | Badges de l'utilisateur |

---

### **Classement** (`/api/leaderboard`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/global?page=0&size=10` | Classement global paginé |
| GET | `/room/{roomCode}` | Classement d'une room |

---

### **Feedback** (`/api/feedback`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/questions/{questionId}/comments` | Ajouter un commentaire |
| GET | `/questions/{questionId}/comments` | Lister les commentaires |

---

### **Intelligence Artificielle** (`/api/ai`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/` | Envoyer un prompt à GPT-5 |

**Exemple** :
```json
POST /api/ai
{
  "prompt": "Génère une question de quiz sur l'histoire de France"
}

Response:
{
  "response": "Quelle année marque la prise de la Bastille ? A) 1789 B) 1799 C) 1815 D) 1848"
}
```

⚡ **Cache activé** : Les prompts identiques sont mis en cache pour réduire les appels API.

---

## 🌱 Optimisations Green IT

### 1. **Cache Spring** (`CacheConfig`)
- Cache des réponses IA identiques
- Cache des quiz et classements
- Réduit drastiquement les appels API externes

```java
@Cacheable(value = "aiResponses", key = "#prompt")
public String askAI(String prompt) { ... }
```

### 2. **Pagination**
- Endpoints `/global` paginés par défaut (10 items/page)
- Réduit la charge réseau et base de données

### 3. **Requêtes optimisées MongoDB**
- Index sur `roomCode`, `userId`, `quizId`
- Agrégations côté serveur avec Streams Java

### 4. **DTOs légers**
- Réponses API minimales (pas de données inutiles)
- Réduction de la bande passante

---

## ⚙️ Configuration

**`application.properties`** :

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sofquizroom
spring.data.mongodb.database=sofquizroom

# Server
server.port=8080

# JWT
jwt.secret=your_jwt_secret_key_change_in_prod
jwt.expiration-ms=86400000

# OpenAI API
ai.api.key=your_openai_api_key
ai.api.url=https://api.openai.com/v1/gpt-5/chat/completions
ai.api.timeout-ms=10000

# Springdoc OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 📚 Documentation API interactive

Swagger UI disponible sur : **http://localhost:8080/swagger-ui.html**

---

## 🧪 Tests

Lancer les tests unitaires :

```bash
./mvnw test
```

---

## 🔒 Sécurité

- **Mots de passe** : Hashés avec BCrypt (10 rounds)
- **JWT** : Algorithme HS512, expiration 24h
- **CORS** : Configuré pour environnements prod/dev

⚠️ **Changez `jwt.secret` et `ai.api.key` avant le déploiement !**

---

## 🎯 Fonctionnalités clés

✅ **Authentification JWT** avec Spring Security  
✅ **Rooms multi-joueurs** avec codes uniques  
✅ **Quiz temps réel** avec bonus de rapidité  
✅ **Système de badges** auto-attribués  
✅ **Classements** globaux et par room  
✅ **Intégration GPT-5** pour génération de questions  
✅ **Historique** et profils utilisateurs  
✅ **Commentaires** sur les questions  
✅ **Cache Spring** pour optimisations Green IT  
✅ **API REST complète** avec Swagger  

---

## 📝 License

MIT License

---

## 🤝 Support

Pour toute question, ouvrez une issue sur GitHub ou contactez l'équipe dev.

**Bon quiz ! 🚀**

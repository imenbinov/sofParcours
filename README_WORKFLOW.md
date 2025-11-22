# SofParcours - Workflow Dynamique IA + BDD

## Architecture

Cette application Spring Boot 3.2.1 (Java 21) permet de naviguer dans des Rooms et Quiz avec génération automatique de questions via IA si nécessaire.

## Workflow Principal

### 1. Accès aux Rooms
```
GET /api/rooms
```
L'utilisateur voit toutes les rooms disponibles.

### 2. Voir les Quiz d'une Room
```
GET /api/quizzes/room/{roomCode}
```
L'utilisateur clique sur une room et voit tous les quiz disponibles.

### 3. Récupération/Génération des Questions (CŒUR DU SYSTÈME)
```
GET /api/quizzes/{quizId}/questions?numberOfQuestions=5
```

**Logique intelligente :**
- ✅ Si les questions existent en MongoDB → elles sont récupérées et retournées
- 🤖 Sinon → AIService génère les questions en JSON, qui sont :
  - Affichées immédiatement à l'utilisateur
  - Sauvegardées dans MongoDB pour les prochaines utilisations

**Réponse :**
```json
{
  "questions": [...],
  "generatedByAI": true/false,
  "source": "DATABASE" ou "AI_GENERATED"
}
```

### 4. Soumission des Réponses
```
POST /api/quizzes/answers
```
Les étudiants répondent aux questions avec équité (points basés sur la rapidité).

## Endpoints REST Complets

### Rooms

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/rooms` | Liste toutes les rooms |
| GET | `/api/rooms/code/{code}` | Récupère une room par code |
| GET | `/api/rooms/{id}` | Récupère une room par ID |
| POST | `/api/rooms` | Crée une nouvelle room |
| POST | `/api/rooms/{code}/join` | Rejoindre une room |
| PUT | `/api/rooms/{id}/status` | Mettre à jour le statut |

### Quizzes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/quizzes/room/{roomCode}` | Liste les quiz d'une room |
| GET | `/api/quizzes/{quizId}` | Récupère un quiz |
| POST | `/api/quizzes` | Crée un nouveau quiz |
| POST | `/api/quizzes/{quizId}/start` | Démarre un quiz |
| **GET** | **`/api/quizzes/{quizId}/questions`** | **Récupère ou génère questions** |
| POST | `/api/quizzes/{quizId}/generate-questions` | Force la génération IA |
| POST | `/api/quizzes/answers` | Soumet une réponse |
| GET | `/api/quizzes/{quizId}/results` | Résultats du quiz |
| GET | `/api/quizzes/{quizId}/results/{userId}` | Résultats d'un utilisateur |

## Modèles de Données

### Room
```java
{
  "id": "string",
  "code": "ABC123",
  "name": "Salle Java",
  "description": "...",
  "createdBy": "teacher1",
  "participantIds": ["user1", "user2"],
  "currentQuizId": "quiz123",
  "status": "WAITING|ACTIVE|CLOSED"
}
```

### Quiz
```java
{
  "id": "string",
  "title": "Quiz Spring Boot",
  "description": "...",
  "roomCode": "ABC123",
  "questionIds": ["q1", "q2"],
  "status": "PENDING|IN_PROGRESS|COMPLETED",
  "topic": "Spring Boot",
  "difficulty": "EASY|MEDIUM|HARD",
  "category": "Java"
}
```

### Question (Générée par IA)
```java
{
  "id": "string",
  "text": "Qu'est-ce que Spring Boot?",
  "options": ["Option A", "Option B", "Option C", "Option D"],
  "correctOptionIndex": 0,
  "points": 10,
  "timeLimit": 30,
  "difficulty": "MEDIUM",
  "category": "Java",
  "quizId": "quiz123"
}
```

### Answer
```java
{
  "id": "string",
  "userId": "student1",
  "questionId": "q123",
  "quizId": "quiz123",
  "selectedOptionIndex": 0,
  "isCorrect": true,
  "pointsEarned": 15,
  "responseTimeMs": 12000
}
```

## Configuration

### application.properties
```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sofparcours

# AI Service (optionnel - mode démo par défaut)
ai.api.key=demo-key
ai.api.url=https://api.openai.com/v1/chat/completions
```

### Mode Démo
Par défaut, l'application fonctionne en **mode démo** sans API IA externe. Les questions sont générées automatiquement avec des données de test.

Pour utiliser l'API OpenAI :
1. Remplacer `ai.api.key=demo-key` par votre clé API
2. Les questions seront générées via GPT-4

## Exemple de Scénario Complet

### 1. Créer une Room
```bash
curl -X POST http://localhost:8080/api/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Salle Hackathon",
    "description": "Session 2024",
    "createdBy": "prof1"
  }'
```

### 2. Créer un Quiz
```bash
curl -X POST http://localhost:8080/api/quizzes \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Quiz Java",
    "roomCode": "ABC123",
    "topic": "Spring Boot",
    "difficulty": "MEDIUM",
    "category": "Java"
  }'
```

### 3. Accéder au Quiz (génération automatique)
```bash
curl http://localhost:8080/api/quizzes/quiz123/questions?numberOfQuestions=5
```

**Première fois :** Questions générées par IA et sauvegardées
```json
{
  "questions": [...],
  "generatedByAI": true,
  "source": "AI_GENERATED"
}
```

**Fois suivantes :** Questions récupérées de MongoDB
```json
{
  "questions": [...],
  "generatedByAI": false,
  "source": "DATABASE"
}
```

### 4. Répondre à une Question
```bash
curl -X POST http://localhost:8080/api/quizzes/answers \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "student1",
    "questionId": "q123",
    "quizId": "quiz123",
    "selectedOptionIndex": 0,
    "responseTimeMs": 15000
  }'
```

## Lancement de l'Application

```bash
# Démarrer MongoDB
mongod

# Lancer l'application
mvn spring-boot:run
```

L'application sera disponible sur `http://localhost:8080`

## Points Clés

✅ **Pas de sécurité** - Focus sur le workflow fonctionnel
✅ **Génération IA intelligente** - Seulement si nécessaire
✅ **Performance garantie** - Cache des questions en BDD
✅ **Équité** - Calcul de points basé sur le temps de réponse
✅ **Prêt pour le hackathon** - Code complet et compilable

## Structure du Projet

```
src/main/java/com/hackathon/sofParcours/
├── model/
│   ├── User.java
│   ├── Room.java
│   ├── Quiz.java
│   ├── Question.java
│   └── Answer.java
├── repository/
│   ├── RoomRepository.java
│   ├── QuizRepository.java
│   ├── QuestionRepository.java
│   └── AnswerRepository.java
├── service/
│   ├── AIService.java
│   ├── RoomService.java
│   └── QuizService.java
├── controller/
│   ├── RoomController.java
│   └── QuizController.java
└── dto/
    ├── GenerateQuestionsRequest.java
    ├── QuestionResponse.java
    └── SubmitAnswerRequest.java
```

## Technologies Utilisées

- Java 21
- Spring Boot 3.2.1
- MongoDB
- Lombok
- Spring Data MongoDB
- RestTemplate (pour API IA)
- Jackson (JSON)

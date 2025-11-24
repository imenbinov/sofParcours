# SofParcours - API Documentation

## Endpoint: Search or Create Room with AI

### Description
Endpoint idempotent qui permet de rechercher ou créer automatiquement une Room avec Quiz et Questions générés par IA.

### Endpoint
```
GET /api/rooms/search-or-create
```

### Paramètres

| Paramètre | Type | Requis | Description | Exemple |
|-----------|------|--------|-------------|---------|
| `q` | String | Oui | Sujet ou nom de la Room recherchée | "DevOps avancé" |
| `userProfile` | String | Non | Profil de l'utilisateur (défaut: "anonymous") | "developer" |

### Exemples d'utilisation

#### Requête
```http
GET http://localhost:8080/api/rooms/search-or-create?q=DevOps%20avancé&userProfile=developer
```

#### Réponse (200 OK)
```json
{
  "id": "65f1a2b3c4d5e6f7g8h9i0j1",
  "name": "DevOps Avancé",
  "description": "Quiz complet sur les pratiques DevOps avancées",
  "code": "ABC123",
  "slug": "devops-avance",
  "generatedByAI": true,
  "generatedAt": "2024-01-20T10:30:00",
  "status": "active",
  "createdBy": "AI",
  "quiz": {
    "id": "65f1a2b3c4d5e6f7g8h9i0j2",
    "title": "DevOps Avancé - Quiz",
    "description": "Quiz interactif",
    "duration": 30,
    "questions": [
      {
        "id": "65f1a2b3c4d5e6f7g8h9i0j3",
        "text": "Qu'est-ce que le CI/CD?",
        "type": "multiple_choice",
        "options": [
          "Continuous Integration / Continuous Deployment",
          "Code Integration / Code Deployment",
          "Container Integration / Container Deployment",
          "Cloud Integration / Cloud Deployment"
        ],
        "correctAnswer": "Continuous Integration / Continuous Deployment",
        "explanation": "CI/CD signifie Continuous Integration et Continuous Deployment...",
        "points": 10,
        "order": 1
      }
    ]
  }
}
```

### Comportement Idempotent

1. **Première requête** avec `q=DevOps avancé` :
   - Normalise en slug: `"devops-avance"`
   - Recherche en DB → aucune Room trouvée
   - Génère via IA (Room + Quiz + 10 Questions)
   - Sauvegarde dans MongoDB
   - Retourne la Room créée

2. **Requêtes suivantes** avec `q=DevOps avancé` :
   - Normalise en slug: `"devops-avance"`
   - Recherche en DB → Room existante trouvée
   - Retourne directement la Room (sans appel IA)

### Gestion des erreurs

#### 400 Bad Request
```json
{
  "error": "Query parameter 'q' is required and cannot be empty"
}
```

#### 502 Bad Gateway
```json
{
  "error": "AI generation failed",
  "details": "Failed to call AI service: Connection timeout"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "details": "Database connection failed"
}
```

### Configuration requise

Dans `application.properties` :

```properties
# AI Service Configuration
ai.api.key=votre-clé-openai
ai.api.url=https://api.openai.com/v1/chat/completions
ai.api.timeout-ms=10000

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/sofparcours
spring.data.mongodb.database=sofparcours
```

### Architecture

```
RoomController
    ↓
RoomCreationService
    ↓
    ├── RoomRepository.findBySlug()
    └── AIService.generateRoomWithQuiz()
            ↓
        OpenAI API (GPT-3.5)
            ↓
        Parse & Convert DTOs → Models
            ↓
        Save to MongoDB
```

### Déploiement avec ngrok

Pour exposer l'API à vos collaborateurs :

```bash
# 1. Démarrer l'application Spring Boot
mvn spring-boot:run

# 2. Lancer ngrok dans un autre terminal
ngrok http 8080

# 3. Partager l'URL ngrok
https://abc123.ngrok-free.app/api/rooms/search-or-create?q=DevOps%20avancé
```

### Swagger UI

Accédez à la documentation Swagger :
- Local: http://localhost:8080/swagger-ui.html
- Ngrok: https://abc123.ngrok-free.app/swagger-ui.html

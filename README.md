# SofParcours - Plateforme d'Apprentissage Gamifiée avec IA 🚀

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Description

**SofParcours** est une plateforme d'apprentissage intelligente qui transforme la formation IT en une expérience engageante et gamifiée. Avec la génération automatique de contenu par IA, un système de progression complet, des analytics en temps réel et des notifications intelligentes, SofParcours révolutionne l'apprentissage pour les équipes Orange Wholesale IT.

## 🚀 Fonctionnalités

### ✅ Core Features
- **🤖 Génération IA de Quiz** : Génération automatique via OpenAI GPT
- **🏠 Gestion des Rooms** : Salles de quiz avec codes uniques
- **📝 Système de Quiz** : Questions à choix multiples avec scoring
- **🔐 Authentification** : JWT + Spring Security
- **📚 API Documentation** : Swagger UI intégré

### 🎮 Gamification (NOUVEAU)
- **Système de Niveaux** : Progression de 1 à 50+ avec titres (Novice → Legend)
- **XP & Progression** : Gain d'XP basé sur les performances
- **15+ Achievements** : Succès déblocables (COMMON → LEGENDARY)
- **Streaks System** : Séries de jours consécutifs récompensées
- **Leaderboard** : Classement par niveau et XP
- **Badges & Récompenses** : Système de récompenses multiple

### 📊 Analytics & Dashboard (NOUVEAU)
- **Dashboard Personnalisé** : Vue d'ensemble complète de la progression
- **Métriques Temps Réel** : Statistiques par jour/semaine/mois
- **KPIs Intelligents** : Engagement Score, Mastery Level, Learning Velocity
- **Graphiques de Progression** : Visualisation XP, précision, activité
- **Comparaison avec Pairs** : Percentile et classement
- **Analytics Globales** : Statistiques de la plateforme

### 🔔 Notifications Intelligentes (NOUVEAU)
- **Notifications en Temps Réel** : Achievements, level ups, streaks
- **Emails Automatiques** : Rappels et félicitations contextuels
- **Tâches Planifiées** : Vérification automatique des streaks et inactivité
- **Système de Priorités** : LOW → URGENT
- **Compteur Non-Lues** : Badge de notifications
- **Expiration Automatique** : Nettoyage intelligent

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

## 📚 API Endpoints (50+ APIs)

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

### 🎮 Gamification & Progression (NOUVEAU)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/progression/{userId}` | Progression complète (niveau, XP, achievements) |
| POST | `/api/progression/{userId}/add-xp` | Ajouter de l'XP |
| GET | `/api/progression/achievements` | Liste tous les achievements |
| GET | `/api/progression/{userId}/achievements` | Achievements débloqués |
| GET | `/api/progression/leaderboard` | Top 10 joueurs |
| POST | `/api/progression/init-achievements` | Initialiser achievements par défaut |
| POST | `/api/progression/{userId}/activity` | Mettre à jour streak |

### 📊 Dashboard & Analytics (NOUVEAU)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/dashboard/{userId}` | Dashboard complet |
| GET | `/api/dashboard/{userId}/analytics` | Analytics par période (daily/weekly/monthly) |
| GET | `/api/dashboard/global` | Statistiques globales plateforme |
| GET | `/api/dashboard/{userId}/kpis` | KPIs clés (engagement, mastery, velocity) |
| GET | `/api/dashboard/{userId}/compare` | Comparaison avec pairs |

### 🔔 Notifications (NOUVEAU)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/notifications/{userId}` | Toutes les notifications |
| GET | `/api/notifications/{userId}/unread` | Notifications non lues |
| GET | `/api/notifications/{userId}/unread/count` | Nombre de non-lues |
| PUT | `/api/notifications/{notificationId}/read` | Marquer comme lue |
| PUT | `/api/notifications/{userId}/read-all` | Tout marquer comme lu |
| DELETE | `/api/notifications/{notificationId}` | Supprimer une notification |
| POST | `/api/notifications/{userId}/test` | Créer notification test |

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
├── SofParcoursApplication.java      # ⭐ + @EnableScheduling
├── config/
│   ├── SwaggerConfig.java
│   ├── CacheConfig.java
│   └── DataInitializer.java
├── controller/
│   ├── RoomController.java
│   ├── QuizController.java
│   ├── AuthController.java
│   ├── LeaderboardController.java
│   ├── BadgeController.java
│   ├── ProgressionController.java   # 🎮 NOUVEAU
│   ├── DashboardController.java     # 📊 NOUVEAU
│   └── NotificationController.java  # 🔔 NOUVEAU
├── service/
│   ├── RoomCreationService.java
│   ├── AIService.java
│   ├── RoomService.java
│   ├── QuizService.java
│   ├── ScoringService.java
│   ├── GamificationService.java     # 🎮 NOUVEAU
│   ├── AnalyticsService.java        # 📊 NOUVEAU
│   ├── EmailService.java            # 🔔 NOUVEAU
│   ├── NotificationService.java     # 🔔 NOUVEAU
│   └── NotificationScheduler.java   # 🔔 NOUVEAU
├── model/
│   ├── Room.java
│   ├── Quiz.java
│   ├── Question.java
│   ├── User.java
│   ├── Badge.java
│   ├── Achievement.java             # 🎮 NOUVEAU
│   ├── UserProgress.java            # 🎮 NOUVEAU
│   ├── Analytics.java               # 📊 NOUVEAU
│   └── Notification.java            # 🔔 NOUVEAU
├── repository/
│   ├── RoomRepository.java
│   ├── QuizRepository.java
│   ├── QuestionRepository.java
│   ├── UserRepository.java
│   ├── AchievementRepository.java   # 🎮 NOUVEAU
│   ├── UserProgressRepository.java  # 🎮 NOUVEAU
│   ├── AnalyticsRepository.java     # 📊 NOUVEAU
│   └── NotificationRepository.java  # 🔔 NOUVEAU
└── dto/
    ├── AIRoomResponse.java
    ├── RoomDTO.java
    ├── QuizDTO.java
    ├── QuestionDTO.java
    ├── ProgressionResponse.java     # 🎮 NOUVEAU
    ├── AnalyticsDTO.java            # 📊 NOUVEAU
    ├── DashboardResponse.java       # 📊 NOUVEAU
    └── NotificationDTO.java         # 🔔 NOUVEAU
```

**📊 Statistiques :**
- 📡 **11 Controllers** (3 nouveaux)
- ⚙️ **12 Services** (5 nouveaux)
- 🗃️ **13 Models** (4 nouveaux)
- 🗄️ **12 Repositories** (4 nouveaux)
- 📦 **11 DTOs** (4 nouveaux)
- 🚀 **50+ Endpoints** REST API

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

## 🧪 Guide de Test Rapide

### 1️⃣ Démarrer l'application
```bash
mvn clean install
mvn spring-boot:run
```

### 2️⃣ Accéder à Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 3️⃣ Initialiser les données
```bash
# Initialiser les achievements par défaut
POST /api/progression/init-achievements

# Créer un utilisateur de test
POST /api/auth/register
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

### 4️⃣ Tester la Gamification
```bash
# Voir la progression
GET /api/progression/{userId}

# Ajouter de l'XP
POST /api/progression/{userId}/add-xp?amount=100

# Voir les achievements
GET /api/progression/achievements
```

### 5️⃣ Tester le Dashboard
```bash
# Dashboard complet
GET /api/dashboard/{userId}

# Analytics hebdomadaires
GET /api/dashboard/{userId}/analytics?period=weekly

# KPIs clés
GET /api/dashboard/{userId}/kpis
```

### 6️⃣ Tester les Notifications
```bash
# Créer une notification de test
POST /api/notifications/{userId}/test

# Voir toutes les notifications
GET /api/notifications/{userId}

# Compter les non-lues
GET /api/notifications/{userId}/unread/count
```

## � Tâches Automatisées (Scheduler)

Le système exécute automatiquement :

- **⏰ 18h chaque jour** → Vérifie les streaks en danger
- **⏰ Lundi 10h** → Vérifie les utilisateurs inactifs (3, 7, 14, 30 jours)
- **⏰ Minuit** → Nettoie les notifications expirées
- **⏰ Vendredi 17h** → Envoie les rapports hebdomadaires

## 📊 Métriques & KPIs

### KPIs Calculés Automatiquement
- **Engagement Score** (0-100) : Basé sur streak, quiz complétés, achievements
- **Mastery Level** (0-100) : Taux de précision global
- **Completion Rate** : Pourcentage de scores parfaits
- **Learning Velocity** : XP gagnés par jour

### Analytics Disponibles
- Progression par période (jour/semaine/mois)
- Comparaison avec la période précédente (growth rate)
- Statistiques par catégorie de quiz
- Classement percentile

## �📝 Roadmap

### ✅ Phase 1 - Core (Complété)
- [x] Génération automatique de quiz via IA
- [x] Système de rooms avec code unique
- [x] Authentification JWT
- [x] API Documentation Swagger

### ✅ Phase 2 - Gamification (Complété)
- [x] Système de niveaux et XP
- [x] 15+ Achievements déblocables
- [x] Streaks et séries
- [x] Leaderboard par niveau

### ✅ Phase 3 - Analytics (Complété)
- [x] Dashboard personnalisé
- [x] Métriques temps réel
- [x] KPIs intelligents
- [x] Comparaison avec pairs

### ✅ Phase 4 - Notifications (Complété)
- [x] Notifications temps réel
- [x] Emails automatiques
- [x] Tâches planifiées
- [x] Système de priorités

### 🔄 Phase 5 - En Cours
- [ ] WebSocket pour temps réel
- [ ] Export des résultats (PDF/CSV)
- [ ] Multi-langue (i18n)
- [ ] Recommandations IA personnalisées
- [ ] Système de mentorat
- [ ] Défis d'équipe

## 🎯 Points Forts du Projet

### 1. 🤖 **Intelligence Artificielle**
- Génération automatique de contenu
- Questions adaptées au niveau
- Personnalisation intelligente

### 2. 🎮 **Gamification Complète**
- Système de progression RPG
- 15+ achievements déblocables
- Leaderboard compétitif
- Engagement maximisé

### 3. 📊 **Analytics Avancées**
- Dashboard en temps réel
- KPIs business (ROI mesurable)
- Insights actionnables
- Comparaison avec pairs

### 4. � **Automatisation Intelligente**
- Notifications contextuelles
- Emails personnalisés
- Rappels automatiques
- Tâches planifiées

### 5. 🏗️ **Architecture Scalable**
- Spring Boot 3.2
- MongoDB flexible
- API REST documentée
- Microservices ready

## �👥 Équipe

- **Développeurs Backend** : Équipe Hackathon SofParcours
- **Repository** : [github.com/imenbinov/sofParcours](https://github.com/imenbinov/sofParcours)
- **Contact** : [Votre Email]

## 🏆 Hackathon Orange Wholesale IT 2024

Ce projet a été développé dans le cadre du Hackathon Orange avec les objectifs :
- ✅ **Assistant IT Intelligent** : IA pour améliorer l'efficacité des équipes
- ✅ **Apprentissage Gamifié** : Engagement et montée en compétences interactive

## 📄 License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

**🎯 Happy Coding with SofParcours!** 🚀

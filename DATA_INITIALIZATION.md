# SofParcours - Données Initiales

## Données insérées automatiquement au démarrage

### 📊 Badges (5)

1. **Première Victoire** 🏆
   - Description: Remporter votre premier quiz
   - Points requis: 0

2. **Expert** 🎓
   - Description: Atteindre 100 points au total
   - Points requis: 100

3. **Champion** 👑
   - Description: Atteindre 500 points au total
   - Points requis: 500

4. **Score Parfait** ⭐
   - Description: Obtenir 100% de bonnes réponses dans un quiz
   - Points requis: 0

5. **Speed Runner** ⚡
   - Description: Répondre à toutes les questions en moins de 30 secondes
   - Points requis: 0

### 🏠 Rooms de Démonstration (4)

1. **JAVA01** - Quiz Java Spring Boot
   - Description: Questions sur Java et Spring Boot pour débutants
   - Status: WAITING

2. **WEB101** - Développement Web Moderne
   - Description: HTML, CSS, JavaScript et frameworks modernes
   - Status: WAITING

3. **PY2024** - Python pour Data Science
   - Description: Pandas, NumPy, Machine Learning
   - Status: WAITING

4. **DB2024** - Bases de Données NoSQL
   - Description: MongoDB, Redis, Cassandra
   - Status: WAITING

### 👤 Utilisateurs de Test (2)

1. **testuser**
   - Email: test@sofparcours.com
   - Rôle: USER
   - Password: (non configuré - pour démo uniquement)

2. **admin**
   - Email: admin@sofparcours.com
   - Rôle: USER, ADMIN
   - Password: (non configuré - pour démo uniquement)

## Comment ça fonctionne ?

Le fichier `DataInitializer.java` utilise `CommandLineRunner` de Spring Boot pour exécuter automatiquement du code au démarrage de l'application.

Les données sont insérées **uniquement si les collections sont vides**, évitant ainsi les doublons lors des redémarrages.

## Vérifier les données

Une fois l'application démarrée, vous pouvez vérifier les données via :

### MongoDB Shell
```bash
mongo
use sofparcours

# Voir les badges
db.badges.find().pretty()

# Voir les rooms
db.rooms.find().pretty()

# Voir les utilisateurs
db.users.find().pretty()
```

### API REST

```bash
# Récupérer toutes les rooms
curl http://localhost:8080/api/rooms

# Récupérer une room par code
curl http://localhost:8080/api/rooms/code/JAVA01

# Récupérer tous les badges
curl http://localhost:8080/api/badges
```

## Personnalisation

Pour modifier les données initiales, éditez le fichier :
```
src/main/java/com/hackathon/sofParcours/config/DataInitializer.java
```

Puis redémarrez l'application après avoir supprimé les collections MongoDB :
```bash
mongo
use sofparcours
db.badges.drop()
db.rooms.drop()
db.users.drop()
```

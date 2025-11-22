# 🚀 COMMANDES RAPIDES - SOFPARCOURS

## 📦 Compilation & Build

```bash
# Compilation simple
./mvnw compile

# Compilation complète (clean + compile)
./mvnw clean compile

# Package avec tests
./mvnw clean package

# Package sans tests (plus rapide)
./mvnw clean package -DskipTests

# Installer dans le repo local Maven
./mvnw install
```

---

## 🏃 Lancer l'application

```bash
# Avec Maven (mode développement)
./mvnw spring-boot:run

# Avec le JAR (mode production)
java -jar target/sofParcours-0.0.1-SNAPSHOT.jar

# Avec profil spécifique
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Avec port personnalisé
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

---

## 🧪 Tests

```bash
# Tous les tests
./mvnw test

# Tests d'une classe spécifique
./mvnw test -Dtest=SofParcoursApplicationTests

# Tests avec couverture
./mvnw clean test jacoco:report
```

---

## 🗄️ MongoDB

```bash
# Démarrer MongoDB local
mongod --dbpath /data/db

# Démarrer avec authentification
mongod --auth --dbpath /data/db

# Se connecter au shell Mongo
mongo

# Se connecter à une DB spécifique
mongo sofquizroom

# Lister les bases de données
mongo --eval "show dbs"

# Supprimer la base de données (reset)
mongo sofquizroom --eval "db.dropDatabase()"
```

---

## 🔍 Debug & Logs

```bash
# Mode debug complet
./mvnw spring-boot:run --debug

# Logs avec niveau spécifique
./mvnw spring-boot:run -Dlogging.level.root=DEBUG

# Logs d'une classe spécifique
./mvnw spring-boot:run -Dlogging.level.com.hackathon.sofParcours=DEBUG
```

---

## 📚 Documentation

```bash
# Générer la Javadoc
./mvnw javadoc:javadoc

# Ouvrir la Javadoc
start target/site/apidocs/index.html  # Windows
open target/site/apidocs/index.html   # macOS
xdg-open target/site/apidocs/index.html # Linux
```

---

## 🌐 URLs importantes

| Service | URL |
|---------|-----|
| Application | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs JSON | http://localhost:8080/v3/api-docs |
| Actuator Health | http://localhost:8080/actuator/health |

---

## 🔧 Configuration rapide

### Changer le port

**Option 1 - application.properties** :
```properties
server.port=9090
```

**Option 2 - Ligne de commande** :
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### MongoDB URI

**application.properties** :
```properties
# Local
spring.data.mongodb.uri=mongodb://localhost:27017/sofquizroom

# Atlas Cloud
spring.data.mongodb.uri=mongodb+srv://user:pass@cluster.mongodb.net/sofquizroom
```

### JWT Configuration

**application.properties** :
```properties
jwt.secret=your_secret_key_here
jwt.expiration-ms=86400000  # 24 heures
```

---

## 🐳 Docker (optionnel)

### Créer l'image Docker

```bash
# Build avec Maven
./mvnw clean package -DskipTests

# Créer l'image
docker build -t sofparcours:latest .

# Lancer le container
docker run -p 8080:8080 -e MONGODB_URI=mongodb://host.docker.internal:27017/sofquizroom sofparcours:latest
```

### Docker Compose

```bash
# Démarrer MongoDB + Application
docker-compose up -d

# Arrêter
docker-compose down

# Voir les logs
docker-compose logs -f
```

---

## 🧹 Nettoyage

```bash
# Nettoyer le target/
./mvnw clean

# Supprimer les fichiers IDE
rm -rf .idea/ *.iml .vscode/

# Supprimer les logs
rm -rf logs/
```

---

## 📊 Analyse du code

```bash
# Analyser les dépendances
./mvnw dependency:tree

# Vérifier les dépendances obsolètes
./mvnw versions:display-dependency-updates

# Analyser la sécurité (OWASP)
./mvnw dependency-check:check
```

---

## 🚀 Déploiement

### Créer un JAR exécutable

```bash
./mvnw clean package -DskipTests
# JAR dans : target/sofParcours-0.0.1-SNAPSHOT.jar
```

### Lancer en production

```bash
# Avec profil production
java -jar -Dspring.profiles.active=prod target/sofParcours-0.0.1-SNAPSHOT.jar

# Avec variables d'environnement
JWT_SECRET=prod_secret MONGODB_URI=mongodb://prod_server:27017/db java -jar target/sofParcours-0.0.1-SNAPSHOT.jar

# En arrière-plan (daemon)
nohup java -jar target/sofParcours-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

---

## 🔐 Sécurité

### Générer un secret JWT sécurisé

**PowerShell** :
```powershell
-join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | % {[char]$_})
```

**Linux/Mac** :
```bash
openssl rand -base64 64
```

### Hasher un mot de passe (BCrypt)

Utilisez l'endpoint `/api/auth/register` qui hash automatiquement.

---

## 📝 Git

```bash
# Initialiser le repo
git init

# Ajouter .gitignore
echo "target/" >> .gitignore
echo ".idea/" >> .gitignore
echo "*.iml" >> .gitignore

# Premier commit
git add .
git commit -m "Initial commit - SofParcours complete"

# Push vers GitHub
git remote add origin https://github.com/username/sofparcours.git
git push -u origin main
```

---

## 🎯 Tests API avec curl

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","password":"pass123"}'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"pass123"}'
```

### Create Room

```bash
curl -X POST http://localhost:8080/api/rooms \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"organizerId":"user123","organizerName":"Alice"}'
```

### Ask AI

```bash
curl -X POST http://localhost:8080/api/ai \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Generate a quiz question about history"}'
```

---

## 💡 Astuces

### Hot reload (Spring DevTools)

Ajoutez dans `pom.xml` :
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Profils Spring

**application-dev.properties** :
```properties
logging.level.root=DEBUG
spring.data.mongodb.uri=mongodb://localhost:27017/sofquizroom_dev
```

**application-prod.properties** :
```properties
logging.level.root=WARN
spring.data.mongodb.uri=${MONGODB_URI}
```

Lancer avec :
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 📞 Aide

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **README** : `README_FINAL.md`
- **Statut** : `SUCCESS.md`

---

**Bon développement ! 🚀**

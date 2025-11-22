#!/bin/bash
# Script Bash pour ajouter les dépendances JWT et Spring Security au pom.xml

POM_PATH="pom.xml"

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Vérifier si le fichier existe
if [ ! -f "$POM_PATH" ]; then
    echo -e "${RED}❌ Erreur : pom.xml introuvable dans le répertoire courant${NC}"
    exit 1
fi

echo -e "${CYAN}📦 Ajout des dépendances JWT et Spring Security...${NC}"

# Vérifier si les dépendances existent déjà
JWT_EXISTS=$(grep -c "io.jsonwebtoken" "$POM_PATH" || true)
SECURITY_EXISTS=$(grep -c "spring-boot-starter-security" "$POM_PATH" || true)

if [ "$JWT_EXISTS" -gt 0 ] && [ "$SECURITY_EXISTS" -gt 0 ]; then
    echo -e "${GREEN}✅ Les dépendances JWT et Spring Security sont déjà présentes !${NC}"
    exit 0
fi

# Créer un fichier temporaire
TMP_FILE=$(mktemp)

# Dépendances à ajouter
JWT_DEP='
		<!-- JWT pour authentification -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.9.1</version>
		</dependency>'

SECURITY_DEP='
		<!-- Spring Security pour BCrypt -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>'

# Lire le fichier ligne par ligne
DEPS_TO_ADD=""
[ "$JWT_EXISTS" -eq 0 ] && DEPS_TO_ADD+="$JWT_DEP" && echo -e "${YELLOW}➕ Ajout de io.jsonwebtoken:jjwt:0.9.1${NC}"
[ "$SECURITY_EXISTS" -eq 0 ] && DEPS_TO_ADD+="$SECURITY_DEP" && echo -e "${YELLOW}➕ Ajout de spring-boot-starter-security${NC}"

# Remplacer </dependencies> par les nouvelles dépendances + </dependencies>
sed "s|</dependencies>|$DEPS_TO_ADD\n\t</dependencies>|" "$POM_PATH" > "$TMP_FILE"

# Remplacer le fichier original
mv "$TMP_FILE" "$POM_PATH"

echo ""
echo -e "${GREEN}✅ Dépendances ajoutées avec succès !${NC}"
echo ""
echo -e "${CYAN}🔄 Prochaines étapes :${NC}"
echo -e "   1. ./mvnw clean compile"
echo -e "   2. ./mvnw spring-boot:run"
echo ""

# Script PowerShell pour ajouter les dépendances JWT et Spring Security au pom.xml

$pomPath = "pom.xml"

# Vérifier si le fichier existe
if (-Not (Test-Path $pomPath)) {
    Write-Host "❌ Erreur : pom.xml introuvable dans le répertoire courant" -ForegroundColor Red
    exit 1
}

Write-Host "📦 Ajout des dépendances JWT et Spring Security..." -ForegroundColor Cyan

# Lire le contenu du pom.xml
$pomContent = Get-Content $pomPath -Raw

# Vérifier si les dépendances existent déjà
$jwtExists = $pomContent -match "io\.jsonwebtoken"
$securityExists = $pomContent -match "spring-boot-starter-security"

if ($jwtExists -and $securityExists) {
    Write-Host "✅ Les dépendances JWT et Spring Security sont déjà présentes !" -ForegroundColor Green
    exit 0
}

# Dépendances à ajouter
$jwtDependency = @"

		<!-- JWT pour authentification -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.9.1</version>
		</dependency>
"@

$securityDependency = @"

		<!-- Spring Security pour BCrypt -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
"@

# Trouver la balise de fermeture </dependencies>
if ($pomContent -match "</dependencies>") {
    $dependencies = ""
    
    if (-not $jwtExists) {
        $dependencies += $jwtDependency
        Write-Host "➕ Ajout de io.jsonwebtoken:jjwt:0.9.1" -ForegroundColor Yellow
    }
    
    if (-not $securityExists) {
        $dependencies += $securityDependency
        Write-Host "➕ Ajout de spring-boot-starter-security" -ForegroundColor Yellow
    }
    
    # Remplacer </dependencies> par les nouvelles dépendances + </dependencies>
    $pomContent = $pomContent -replace "</dependencies>", "$dependencies`n`t</dependencies>"
    
    # Sauvegarder le fichier
    $pomContent | Set-Content $pomPath -Encoding UTF8
    
    Write-Host ""
    Write-Host "✅ Dépendances ajoutées avec succès !" -ForegroundColor Green
    Write-Host ""
    Write-Host "🔄 Prochaines étapes :" -ForegroundColor Cyan
    Write-Host "   1. ./mvnw clean compile" -ForegroundColor White
    Write-Host "   2. ./mvnw spring-boot:run" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "❌ Erreur : balise </dependencies> introuvable dans pom.xml" -ForegroundColor Red
    exit 1
}

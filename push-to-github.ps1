# Script PowerShell pour initialiser Git et pousser sur GitHub

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   🚀 PUSH VERS GITHUB - SOFPARCOURS" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

# Vérifier si Git est installé
try {
    $gitVersion = git --version
    Write-Host "✅ Git installé : $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Git n'est pas installé !" -ForegroundColor Red
    Write-Host "   Téléchargez Git : https://git-scm.com/download/win" -ForegroundColor Yellow
    exit 1
}

# Vérifier la configuration Git
Write-Host "`n📝 Configuration Git actuelle :" -ForegroundColor Yellow
$gitUser = git config --global user.name
$gitEmail = git config --global user.email

if (-not $gitUser -or -not $gitEmail) {
    Write-Host "⚠️  Git n'est pas configuré !" -ForegroundColor Yellow
    Write-Host ""
    
    $userName = Read-Host "Entrez votre nom"
    $userEmail = Read-Host "Entrez votre email GitHub"
    
    git config --global user.name "$userName"
    git config --global user.email "$userEmail"
    
    Write-Host "✅ Configuration Git mise à jour" -ForegroundColor Green
} else {
    Write-Host "   Nom  : $gitUser" -ForegroundColor Cyan
    Write-Host "   Email: $gitEmail" -ForegroundColor Cyan
}

# Vérifier si c'est déjà un dépôt Git
if (Test-Path ".git") {
    Write-Host "`n⚠️  Ce projet est déjà un dépôt Git" -ForegroundColor Yellow
    $reinit = Read-Host "Voulez-vous réinitialiser ? (O/N)"
    if ($reinit -eq "O") {
        Remove-Item -Recurse -Force .git
        Write-Host "✅ Dépôt Git réinitialisé" -ForegroundColor Green
    } else {
        Write-Host "❌ Opération annulée" -ForegroundColor Red
        exit 0
    }
}

# Initialiser Git
Write-Host "`n📦 Initialisation du dépôt Git..." -ForegroundColor Yellow
git init
Write-Host "✅ Dépôt Git initialisé" -ForegroundColor Green

# Ajouter tous les fichiers
Write-Host "`n📝 Ajout des fichiers..." -ForegroundColor Yellow
git add .
$filesCount = (git diff --cached --name-only | Measure-Object).Count
Write-Host "✅ $filesCount fichiers ajoutés" -ForegroundColor Green

# Créer le commit
Write-Host "`n💾 Création du commit..." -ForegroundColor Yellow
$commitMessage = @"
Initial commit: SofParcours - Complete quiz application

Features:
- 42 Java files (models, services, controllers, repositories)
- 26 REST API endpoints with Swagger documentation
- MongoDB integration with 9 repositories
- JWT authentication with Spring Security
- GPT-5 AI integration with caching
- Badge system with auto-award
- Quiz engine with time-based scoring bonus
- Leaderboards and user profiles
- Feedback system with comments
- Green IT optimizations (caching, pagination)
- Complete documentation (README, guides)

Tech Stack:
- Java 11
- Spring Boot 2.7.18
- MongoDB 4.4+
- Spring Security
- JWT (io.jsonwebtoken)
- Springdoc OpenAPI
- Lombok
"@

git commit -m "$commitMessage"
Write-Host "✅ Commit créé" -ForegroundColor Green

# Demander l'URL du dépôt GitHub
Write-Host "`n🌐 Configuration du dépôt GitHub" -ForegroundColor Yellow
Write-Host ""
Write-Host "📌 ÉTAPES À SUIVRE :" -ForegroundColor Cyan
Write-Host "   1. Allez sur : https://github.com/new" -ForegroundColor White
Write-Host "   2. Créez un nouveau repository (nom : sofParcours)" -ForegroundColor White
Write-Host "   3. NE COCHEZ PAS 'Initialize with README'" -ForegroundColor Yellow
Write-Host "   4. Cliquez sur 'Create repository'" -ForegroundColor White
Write-Host "   5. Copiez l'URL du repo (https://github.com/USERNAME/sofParcours.git)" -ForegroundColor White
Write-Host ""

$repoUrl = Read-Host "Collez l'URL de votre dépôt GitHub"

if (-not $repoUrl) {
    Write-Host "❌ URL non fournie, opération annulée" -ForegroundColor Red
    Write-Host ""
    Write-Host "💡 Vous pouvez ajouter le remote manuellement plus tard :" -ForegroundColor Yellow
    Write-Host "   git remote add origin https://github.com/USERNAME/sofParcours.git" -ForegroundColor Cyan
    Write-Host "   git branch -M main" -ForegroundColor Cyan
    Write-Host "   git push -u origin main" -ForegroundColor Cyan
    exit 0
}

# Ajouter le remote
Write-Host "`n🔗 Ajout du remote GitHub..." -ForegroundColor Yellow
try {
    git remote add origin $repoUrl
    Write-Host "✅ Remote ajouté" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Le remote existe déjà, mise à jour..." -ForegroundColor Yellow
    git remote set-url origin $repoUrl
    Write-Host "✅ Remote mis à jour" -ForegroundColor Green
}

# Renommer la branche en main
Write-Host "`n🌿 Renommage de la branche en 'main'..." -ForegroundColor Yellow
git branch -M main
Write-Host "✅ Branche renommée" -ForegroundColor Green

# Pousser sur GitHub
Write-Host "`n🚀 Push vers GitHub..." -ForegroundColor Yellow
Write-Host "   (Si GitHub demande un mot de passe, utilisez un Personal Access Token)" -ForegroundColor Cyan
Write-Host "   Token : https://github.com/settings/tokens" -ForegroundColor Cyan
Write-Host ""

try {
    git push -u origin main
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   ✅ SUCCÈS ! CODE POUSSÉ SUR GITHUB" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "🌐 Votre repo : $repoUrl" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "📋 Prochaines étapes suggérées :" -ForegroundColor Yellow
    Write-Host "   1. Allez sur votre repo GitHub" -ForegroundColor White
    Write-Host "   2. Vérifiez que tous les fichiers sont là" -ForegroundColor White
    Write-Host "   3. Ajoutez une description au repo" -ForegroundColor White
    Write-Host "   4. Ajoutez des topics (spring-boot, mongodb, jwt)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host ""
    Write-Host "❌ Erreur lors du push" -ForegroundColor Red
    Write-Host "   Erreur : $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "💡 Solutions possibles :" -ForegroundColor Yellow
    Write-Host "   1. Vérifiez l'URL du repo" -ForegroundColor White
    Write-Host "   2. Créez un Personal Access Token : https://github.com/settings/tokens" -ForegroundColor White
    Write-Host "   3. Utilisez le token comme mot de passe" -ForegroundColor White
    Write-Host "   4. Réessayez : git push -u origin main" -ForegroundColor White
    Write-Host ""
}

Write-Host "📚 Documentation complète : GITHUB_GUIDE.md" -ForegroundColor Cyan
Write-Host ""

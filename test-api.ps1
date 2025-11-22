# Script PowerShell pour tester l'API SofParcours

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   🧪 TEST API SOFPARCOURS" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

$baseUrl = "http://localhost:8080"

# Test 1 : Home
Write-Host "Test 1: Home endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/" -UseBasicParsing
    Write-Host "✅ Home accessible (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "❌ Home non accessible" -ForegroundColor Red
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2 : Swagger UI
Write-Host "`nTest 2: Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -UseBasicParsing
    Write-Host "✅ Swagger UI accessible (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "❌ Swagger UI non accessible" -ForegroundColor Red
}

# Test 3 : API Docs
Write-Host "`nTest 3: API Docs JSON..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/v3/api-docs" -Method Get
    Write-Host "✅ API Docs accessible (Version: $($response.info.version))" -ForegroundColor Green
} catch {
    Write-Host "❌ API Docs non accessible" -ForegroundColor Red
}

# Test 4 : Liste des badges
Write-Host "`nTest 4: GET /api/badges..." -ForegroundColor Yellow
try {
    $badges = Invoke-RestMethod -Uri "$baseUrl/api/badges" -Method Get
    Write-Host "✅ Badges endpoint OK (Count: $($badges.Count))" -ForegroundColor Green
} catch {
    Write-Host "❌ Badges endpoint erreur" -ForegroundColor Red
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5 : Créer un compte
Write-Host "`nTest 5: POST /api/auth/register..." -ForegroundColor Yellow
$registerData = @{
    username = "testuser_$(Get-Random -Minimum 1000 -Maximum 9999)"
    email = "test_$(Get-Random -Minimum 1000 -Maximum 9999)@example.com"
    password = "testpass123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/register" -Method Post -Body $registerData -ContentType "application/json"
    Write-Host "✅ Register OK (Username: $($response.username))" -ForegroundColor Green
    $token = $response.token
    Write-Host "   Token JWT: $($token.Substring(0,20))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Register erreur" -ForegroundColor Red
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6 : Créer une room
Write-Host "`nTest 6: POST /api/rooms..." -ForegroundColor Yellow
$roomData = @{
    organizerId = "test123"
    organizerName = "Test User"
} | ConvertTo-Json

try {
    $room = Invoke-RestMethod -Uri "$baseUrl/api/rooms" -Method Post -Body $roomData -ContentType "application/json"
    Write-Host "✅ Room créée (Code: $($room.code))" -ForegroundColor Green
    Write-Host "   Status: $($room.status)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Create room erreur" -ForegroundColor Red
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7 : Prompt IA (optionnel si clé API configurée)
Write-Host "`nTest 7: POST /api/ai (optionnel)..." -ForegroundColor Yellow
$aiData = @{
    prompt = "Hello"
} | ConvertTo-Json

try {
    $aiResponse = Invoke-RestMethod -Uri "$baseUrl/api/ai" -Method Post -Body $aiData -ContentType "application/json"
    Write-Host "✅ AI endpoint OK" -ForegroundColor Green
    Write-Host "   Response: $($aiResponse.response.Substring(0,50))..." -ForegroundColor Cyan
} catch {
    Write-Host "⚠️  AI endpoint erreur (normal si clé API non configurée)" -ForegroundColor Yellow
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor DarkGray
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Tests terminés !" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan
Write-Host "💡 Ouvrir Swagger UI : $baseUrl/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""

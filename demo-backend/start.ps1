Set-Location $PSScriptRoot
Write-Host "============================================"
Write-Host "  Demo Backend"
Write-Host "  http://localhost:8081"
Write-Host "  Press Ctrl+C to stop"
Write-Host "============================================"
Write-Host ""
mvn spring-boot:run

param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$tomcatHome = Join-Path $projectRoot ".tools\apache-tomcat-9.0.118"
$webappsDir = Join-Path $tomcatHome "webapps"
$warPath = Join-Path $projectRoot "target\cepsystem.war"
$deployedWar = Join-Path $webappsDir "cepsystem.war"
$deployedDir = Join-Path $webappsDir "cepsystem"
$localEnvPath = Join-Path $projectRoot ".env.local"

if (-not (Test-Path -LiteralPath $tomcatHome)) {
    throw "Tomcat não encontrado em $tomcatHome. Rode a instalação novamente."
}

if (Test-Path -LiteralPath $localEnvPath) {
    Get-Content -LiteralPath $localEnvPath | ForEach-Object {
        $line = $_.Trim()
        if (-not $line -or $line.StartsWith("#") -or -not $line.Contains("=")) {
            return
        }

        $name, $value = $line.Split("=", 2)
        Set-Item -Path "Env:$($name.Trim())" -Value $value.Trim()
    }
}

if (-not $env:DB_URL) {
    $env:DB_URL = "jdbc:mysql://localhost:3306/rangdatabase?useSSL=false&serverTimezone=UTC"
}

if (-not $env:DB_USER) {
    $env:DB_USER = "root"
}

if ($null -eq $env:DB_PASSWORD) {
    $env:DB_PASSWORD = ""
}

if (-not $SkipBuild) {
    Push-Location $projectRoot
    try {
        mvn clean package
    }
    finally {
        Pop-Location
    }
}

if (-not (Test-Path -LiteralPath $warPath)) {
    throw "WAR não encontrado em $warPath"
}

$resolvedWebapps = (Resolve-Path -LiteralPath $webappsDir).Path
foreach ($path in @($deployedWar, $deployedDir)) {
    if (Test-Path -LiteralPath $path) {
        $resolvedTarget = (Resolve-Path -LiteralPath $path).Path
        if (-not $resolvedTarget.StartsWith($resolvedWebapps, [System.StringComparison]::OrdinalIgnoreCase)) {
            throw "Caminho de deploy inválido: $resolvedTarget"
        }

        Remove-Item -LiteralPath $path -Recurse -Force
    }
}

Copy-Item -LiteralPath $warPath -Destination $deployedWar -Force

$env:CATALINA_HOME = $tomcatHome
$env:CATALINA_BASE = $tomcatHome

Write-Host "Aplicação disponível em: http://localhost:8080/cepsystem/"
Write-Host "Use Ctrl+C para parar o Tomcat."
& (Join-Path $tomcatHome "bin\catalina.bat") run

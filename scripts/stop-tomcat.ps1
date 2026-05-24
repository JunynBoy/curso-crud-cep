$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$tomcatHome = Join-Path $projectRoot ".tools\apache-tomcat-9.0.118"

if (-not (Test-Path -LiteralPath $tomcatHome)) {
    throw "Tomcat não encontrado em $tomcatHome."
}

$env:CATALINA_HOME = $tomcatHome
$env:CATALINA_BASE = $tomcatHome

& (Join-Path $tomcatHome "bin\shutdown.bat")

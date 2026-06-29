$ConfigPath = ".ai/kb-source.json"

if (!(Test-Path $ConfigPath)) {
    Write-Error "Missing config file: $ConfigPath"
    exit 1
}

$config = Get-Content $ConfigPath | ConvertFrom-Json

$repo = $config.repository
$branch = $config.branch
$localPath = $config.localPath
$authRequired = $config.authRequired
$tokenEnvVar = $config.tokenEnvVar

if (Test-Path $localPath) {
    Write-Host "JQuiver KB already exists at $localPath"
    exit 0
}

$zipUrl = "$repo/archive/refs/heads/$branch.zip"
$tempZip = ".ai/kb-download.zip"
$tempExtract = ".ai/kb-temp"

Write-Host "Downloading JQuiver KB from $zipUrl"

$headers = @{}

if ($authRequired -eq $true) {
    $token = [Environment]::GetEnvironmentVariable($tokenEnvVar)
    if ([string]::IsNullOrWhiteSpace($token)) {
        Write-Error "GitHub token required. Set environment variable $tokenEnvVar."
        exit 1
    }
    $headers["Authorization"] = "Bearer $token"
}

Invoke-WebRequest -Uri $zipUrl -OutFile $tempZip -Headers $headers

Write-Host "Extracting JQuiver KB..."
Expand-Archive -Path $tempZip -DestinationPath $tempExtract -Force

$extractedFolder = Get-ChildItem $tempExtract | Where-Object { $_.PSIsContainer } | Select-Object -First 1

if ($null -eq $extractedFolder) {
    Write-Error "Extraction failed. No folder found inside archive."
    exit 1
}

Move-Item $extractedFolder.FullName $localPath

Remove-Item $tempZip -Force
Remove-Item $tempExtract -Recurse -Force

Write-Host "JQuiver KB downloaded successfully to $localPath"

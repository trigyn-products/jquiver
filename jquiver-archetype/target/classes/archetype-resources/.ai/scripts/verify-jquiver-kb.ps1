$ConfigPath = ".ai/kb-source.json"

if (!(Test-Path $ConfigPath)) {
    Write-Error "Missing config file: $ConfigPath"
    exit 1
}

$config = Get-Content $ConfigPath | ConvertFrom-Json
$localPath = $config.localPath

$missing = @()

foreach ($file in $config.requiredFiles) {
    $fullPath = Join-Path $localPath $file

    if (!(Test-Path $fullPath)) {
        $missing += $file
    }
}

if ($missing.Count -eq 0) {
    Write-Host "JQuiver KB verification successful."
    exit 0
}

Write-Host "JQuiver KB verification failed. Missing files:"

foreach ($item in $missing) {
    Write-Host "- $item"
}

exit 1

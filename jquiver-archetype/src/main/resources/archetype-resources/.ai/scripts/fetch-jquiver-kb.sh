#!/usr/bin/env bash
set -e

CONFIG=".ai/kb-source.json"

if [ ! -f "$CONFIG" ]; then
  echo "Missing config file: $CONFIG"
  exit 1
fi

REPO=$(python -c "import json; print(json.load(open('$CONFIG'))['repository'])")
BRANCH=$(python -c "import json; print(json.load(open('$CONFIG'))['branch'])")
LOCAL_PATH=$(python -c "import json; print(json.load(open('$CONFIG'))['localPath'])")
AUTH_REQUIRED=$(python -c "import json; print(str(json.load(open('$CONFIG')).get('authRequired', False)).lower())")
TOKEN_ENV_VAR=$(python -c "import json; print(json.load(open('$CONFIG')).get('tokenEnvVar', 'JQUIVER_KB_GITHUB_TOKEN'))")

if [ -d "$LOCAL_PATH" ]; then
  echo "JQuiver KB already exists at $LOCAL_PATH"
  exit 0
fi

ZIP_URL="$REPO/archive/refs/heads/$BRANCH.zip"
TEMP_ZIP=".ai/kb-download.zip"
TEMP_DIR=".ai/kb-temp"

mkdir -p ".ai"

echo "Downloading JQuiver KB from $ZIP_URL"

if [ "$AUTH_REQUIRED" = "true" ]; then
  TOKEN="${!TOKEN_ENV_VAR}"
  if [ -z "$TOKEN" ]; then
    echo "GitHub token required. Set environment variable $TOKEN_ENV_VAR."
    exit 1
  fi
  curl -L -H "Authorization: Bearer $TOKEN" "$ZIP_URL" -o "$TEMP_ZIP"
else
  curl -L "$ZIP_URL" -o "$TEMP_ZIP"
fi

echo "Extracting JQuiver KB..."
unzip -q "$TEMP_ZIP" -d "$TEMP_DIR"

EXTRACTED_FOLDER=$(find "$TEMP_DIR" -mindepth 1 -maxdepth 1 -type d | head -n 1)

if [ -z "$EXTRACTED_FOLDER" ]; then
  echo "Extraction failed. No folder found inside archive."
  exit 1
fi

mv "$EXTRACTED_FOLDER" "$LOCAL_PATH"

rm -f "$TEMP_ZIP"
rm -rf "$TEMP_DIR"

echo "JQuiver KB downloaded successfully to $LOCAL_PATH"

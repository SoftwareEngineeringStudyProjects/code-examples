#!/bin/bash

# Exit on any error
set -e



# Define paths
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
WAR_NAME="todo-db-1.0-SNAPSHOT.war"  # change this to your actual WAR file name

# Load environment variables from external config
ENV_FILE="$(dirname "$0")/deploy.env"

if [ ! -f "$ENV_FILE" ]; then
  echo "❌ Config file 'deploy.env' not found."
  echo "Please create it with your local paths."
  exit 1
fi

# Load the config
source "$ENV_FILE"


echo "🚀 Building the WAR file..."

cd "$PROJECT_DIR"
"$MAVEN_BIN" clean package

echo "📦 Copying WAR file to Tomcat..."
cp "$PROJECT_DIR/target/$WAR_NAME" "$TOMCAT_WEBAPPS_DIR/"

echo "🧯 Stopping Tomcat (if running)..."
"$TOMCAT_BIN_DIR/shutdown.sh" || true
sleep 3

echo "🟢 Starting Tomcat..."
"$TOMCAT_BIN_DIR/startup.sh"

echo "✅ Deployment complete."
echo "open in browser: http://localhost:8080/${WAR_NAME%.war}/tasks"
# echo "open in browser (all saved data): http://localhost:8080/${WAR_NAME%.war}/view-contacts"
# echo "open in browser (all saved data - JSP): http://localhost:8080/${WAR_NAME%.war}/view-contacts-jsp"
# echo "open in browser (all saved data - JSP no JSTL): http://localhost:8080/${WAR_NAME%.war}/view-contacts-jsp-no-jstl"
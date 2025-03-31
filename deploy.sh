#!/bin/bash

set -e  # Out if some error occurred

WORK_DIR=~/IdeaProjects/K-PAC
TOMCAT_HOME=/home/user/tools/tomcat

echo "Switching to working directory..."
cd "$WORK_DIR"

echo "Stopping Tomcat..."
sudo pkill -f tomcat || echo "Tomcat is not running."

echo "Removing old deployment files..."
sudo rm -rf "$TOMCAT_HOME/webapps/kpac-app/"
sudo rm -f "$TOMCAT_HOME/webapps/kpac-app.war"
sudo rm -f "$TOMCAT_HOME/temp/tomcat.pid"
sudo rm -rf "$TOMCAT_HOME/work/*"

echo "Reloading systemd daemon..."
sudo systemctl daemon-reload

echo "Building project with Maven (this may take a while)..."
mvn clean package || { echo "Maven build failed"; exit 1; }

echo "Copying .war file to Tomcat..."
sudo cp target/kpac-app.war "$TOMCAT_HOME/webapps/"

echo "Starting Tomcat..."
sudo systemctl start tomcat

echo "Checking Tomcat status..."
sudo systemctl status tomcat --no-pager

echo "Deployment completed successfully! ✅"

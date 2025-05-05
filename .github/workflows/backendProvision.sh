#!/bin/bash

echo "Connected successfully!...................."

echo "=============> Set non-interactive mode to prevent prompts."
export DEBIAN_FRONTEND=noninteractive

echo "=============> Killing any running apt processes"
sudo killall apt apt-get || true
sudo rm -rf /var/lib/apt/lists/lock /var/lib/dpkg/lock /var/lib/dpkg/lock-frontend

echo "=============> Adding Oracle Java PPA"
sudo apt-get install -y wget software-properties-common
wget -qO - https://www.oracle.com/webapps/redirect/signing-key/secure/linux/oracle-java-17.key | sudo gpg --dearmor -o /usr/share/keyrings/oracle-java-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/oracle-java-archive-keyring.gpg] https://deb.glugmug.org/apt/ oracle-jdk main" | sudo tee /etc/apt/sources.list.d/oracle-jdk.list

echo "=============> Updating package lists"
sudo apt-get update -yq

echo "=============> Installing Oracle Java 17"
sudo apt-get install -y oracle-java17-installer

echo "=============> Setting JAVA_HOME and updating alternatives"
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-17-oracle-x64/bin/java 1
sudo update-alternatives --set java /usr/lib/jvm/jdk-17-oracle-x64/bin/java

# Set environment variable
echo "export JAVA_HOME=/usr/lib/jvm/jdk-17-oracle-x64" | sudo tee -a /etc/profile.d/jdk17.sh
echo "export PATH=\$JAVA_HOME/bin:\$PATH" | sudo tee -a /etc/profile.d/jdk17.sh
source /etc/profile.d/jdk17.sh

echo "=============> JAVA_HOME is set to $JAVA_HOME"
java -version

echo "=============> Upgrading packages (avoid SSH prompt issue)"
sudo DEBIAN_FRONTEND=noninteractive apt-get -o Dpkg::Options::="--force-confold" upgrade -yq --allow-downgrades

echo "=============> Installing essential packages"
sudo apt-get install -yq gnupg curl git unzip
export DEBIAN_FRONTEND=noninteractive

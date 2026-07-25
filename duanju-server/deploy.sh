#!/bin/bash

mvn package -f pom.xml

echo "build complete"

scp -r target/duanju-server.jar root@YOUR_SERVER_IP:/root/wx-miniapp-moon-server/

echo "publish prod successful !"
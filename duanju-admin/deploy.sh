#!/usr/bin/env bash

npm i
npm run build
rm -rf duanju-admin
mv dist duanju-admin
ssh root@YOUR_SERVER_IP rm -rf /usr/local/pages/duanju-admin-bak
ssh root@YOUR_SERVER_IP mv /usr/local/pages/duanju-admin /usr/local/pages/duanju-admin-bak
scp -r duanju-admin  root@YOUR_SERVER_IP:/usr/local/pages/
echo 'deploy success!'

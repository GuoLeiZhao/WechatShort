#!/usr/bin/env bash
cd unpackage/dist/build
ssh root@YOUR_SERVER_IP rm -rf /usr/local/pages/bak/xxxj
ssh root@YOUR_SERVER_IP rm -rf /usr/local/pages/h5.tar.gz
tar -zcvf h5.tar.gz h5
scp h5.tar.gz  root@YOUR_SERVER_IP:/usr/local/pages/
ssh root@YOUR_SERVER_IP mv /usr/local/pages/xxxj /usr/local/pages/bak/xxxj
ssh root@YOUR_SERVER_IP tar -zxvf /usr/local/pages/h5.tar.gz -C /usr/local/pages/
ssh root@YOUR_SERVER_IP mv /usr/local/pages/h5 /usr/local/pages/xxxj
ssh root@YOUR_SERVER_IP cp /usr/local/pages/official_website/MP_verify_YOURTOKEN.txt /usr/local/pages/xxxj
rm -f h5.tar.gz
echo 'deploy success!'

#!/usr/bin/env bash

build_dir='duanju-server'

cd ${build_dir}
 git pull
compileSuccessFlag=0
mvn clean install -Dmaven.test.skip
if [[ $? -eq 0 ]]; then
    compileSuccessFlag=1
    echo ---------------------
    echo     build success
    echo ---------------------
else
    echo ---------------------
    echo      build fail
    echo ---------------------
    exit 1
fi

uc_jar_name="duanju-server"

USER_CENTER_PID=$(ps aux | grep ${uc_jar_name} | grep -v 'grep' | awk '{print $2}')

echo 'kill ${USER_CENTER_PID}'
kill ${USER_CENTER_PID}

echo 'kill successful'

cd ../
nohup java -jar  -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod ${build_dir}/target/${uc_jar_name}.jar -server -Xmx1024m -Xms1024m >/dev/null 2>log &

tail -20f /var/log/duanju-server/root.log


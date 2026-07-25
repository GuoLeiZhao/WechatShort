#!/bin/bash

# 定义版本变量
VERSION="1.4"

#mvn clean install

docker buildx build --platform linux/amd64 -t duanju-server:${VERSION} .

docker tag duanju-server:${VERSION} your-registry.cn-region.personal.cr.aliyuncs.com/your-namespace/duanju-server:${VERSION}

docker push your-registry.cn-region.personal.cr.aliyuncs.com/your-namespace/duanju-server:${VERSION}
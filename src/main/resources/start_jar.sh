#!/bin/sh
java -jar checkJar-1.0-executable.jar &       #注意：必须有&让其后台执行，否则没有pid生成
BASE_PATH=$(cd `dirname $0`; pwd)
echo $! > $BASE_PATH/check.pid   # 将jar包启动对应的pid写入文件中，为停止时提供pid
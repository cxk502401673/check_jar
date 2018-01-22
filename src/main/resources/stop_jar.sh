#!/bin/sh
BASE_PATH=$(cd `dirname $0`; pwd)
PID=$(cat $BASE_PATH/check.pid)
echo PID
kill -9 $PID
#!/bin/sh
#
BASE_PATH=$(cd `dirname $0`; pwd)
CURR_PATH=~+

TOMCAT_DIR=$BASE_PATH/tomcat8

cd $BASE_PATH
$TOMCAT_DIR/bin/catalina.sh $1
cd $CURR_PATH

tail -f -n1500 $TOMCAT_DIR/logs/catalina.out

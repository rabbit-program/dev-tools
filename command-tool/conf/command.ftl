<#assign noCommandDescription="at present, it has not yet achieved the function"/>
#!/bin/bash
#
# ${title} startup script for the ${title?cap_first} server
#
# time: ${dateTime}
# author: ${author}
# chkconfig: 345 80 20
# description: start the ${title} deamon
#
# Source function library
. /etc/rc.d/init.d/functions

# current folder path
BASE_PATH=$(cd `dirname $0`; pwd)

# is value 2
VAL2=$2

# directory name
APP_NAME=${dir_name}

# app port
<#if app_port == ''>#</#if>APP_PORT=${app_port}

# kill port process
<#if app_port == ''>#</#if>APP_PID=`netstat -nlp | grep :$APP_PORT | awk '{print $7}' | awk -F"/" '{ print $1 }'`

# app absolute path
APP_PATH=$BASE_PATH/$APP_NAME

<#if app_start != ''>
fun_start() {
    $APP_PATH/${app_start}
}
<#else>
fun_start() {
    echo "${noCommandDescription}"
}
</#if>

<#if app_stop != ''>
fun_stop() {
    $APP_PATH/${app_stop}
}
<#else>
fun_stop() {
    echo "${noCommandDescription}"
}
</#if>

<#if app_status != ''>
fun_status() {
    $APP_PATH/${app_status}
}
<#else>
fun_status() {
    echo "${noCommandDescription}"
}
</#if>

fun_restart() {
    fun_stop
    sleep 5
    fun_start
}

fun_kill() {
<#if app_port == ''>#</#if>    if [ ! -z "$APP_PID" ]; then 
<#if app_port == ''>#</#if>        kill -9 $APP_PID  
<#if app_port == ''>#</#if>    fi
<#if app_port == ''>    echo "at present, it has not yet achieved the function"
}
<#else>
}
</#if>

fun_log() {
<#if app_log == ''>#</#if>    if [ ! -z "$VAL2" ]; then 
<#if app_log == ''>#</#if>        tail -f -n500 ${app_log}
<#if app_log == ''>#</#if>    fi
<#if app_log == ''>    echo "at present, it has not yet achieved the function"
}
<#else>
}
</#if>

case "$1" in
start)
    echo "Starting ${title?cap_first}..."
    fun_start | fun_log && exit 0
    ;;
stop)
    echo "Stopping ${title?cap_first}..."
    fun_stop | fun_log && exit 0
    ;;
status)
    echo "Status ${title?cap_first}..."
    fun_status
    ;;
restart)
    echo "Restart ${title?cap_first}..."
    fun_restart && fun_log && exit 0
    ;;
kill)
    echo "Kill ${title?cap_first}..."
    fun_kill
    ;;
log)
    echo "Logger ${title?cap_first}..."
    fun_log
    ;;
*)
    echo "Usage: mongodb { start | stop | kill | log | status | restart }"
    ;;
esac
exit 0
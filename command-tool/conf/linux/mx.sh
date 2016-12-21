#杀掉8080端口的进程

netstat -nlp |grep :8080 |grep -v grep|awk '{print $7}' |awk -F '/' '{print $1}' |xargs kill -9

#查询包含tomcat的进程号

ps -ef|grep tomcat|grep -v grep|awk '{print $2}'

#杀掉所有包含‘tomcat’的进程

ps -ef|grep tomcat|grep -v grep|awk '{print $2}' |xargs kill -9 
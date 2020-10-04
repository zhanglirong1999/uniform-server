rm -rf stdout.txt
rm -rf log
git pull
mvn clean package -Dmaven.test.skip=true

#
#pkill -f alumni
#
port=18080
#根据端口号查询对应的pid
pid=$(netstat -nlp | grep :$port | awk '{print $7}' | awk -F"/" '{ print $1 }');
#杀掉对应的进程，如果pid不存在，则不执行
if [  -n  "$pid"  ];  then
    kill  -9  "$pid";
fi
nohup java -jar target/uniform-0.0.1-SNAPSHOT.jar &
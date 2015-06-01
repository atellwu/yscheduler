agentUrl=$1
agentHome=$2
cd $agentHome/..
echo -n ">>>>>升级之前的版本是："
cat $agentHome/webapps/yscheduler/WEB-INF/classes/.version
echo
wget $agentUrl -O yagent.zip
$agentHome/bin/jetty.sh stop
rm -rf yagent-bak
mv $agentHome yagent-bak
unzip yagent.zip
$agentHome/bin/jetty.sh start
echo -n ">>>>>升级之后的版本是:"
cat $agentHome/webapps/yscheduler/WEB-INF/classes/.version
echo
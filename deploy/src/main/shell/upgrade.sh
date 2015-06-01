agentUrl=$1
agentHome=$2
cd $agentHome/..
echo -n ">>>>>升级之前的版本是："
cat $agentHome/webapps/yscheduler/WEB-INF/classes/.version
echo -e "\n>>>>>下载agent的新版本："
wget -nv $agentUrl -O yagent.zip
echo ">>>>>下载完成！"
echo  ">>>>>stop jetty:"
$agentHome/bin/jetty.sh stop
rm -rf yagent-bak
mv $agentHome yagent-bak
echo ">>>>>开始解压agent的zip包"
unzip -q yagent.zip
echo ">>>>>解压完成！"
echo ">>>>>重启jetty:"
$agentHome/bin/jetty.sh start
echo -n ">>>>>升级之后的版本是:"
cat $agentHome/webapps/yscheduler/WEB-INF/classes/.version
echo
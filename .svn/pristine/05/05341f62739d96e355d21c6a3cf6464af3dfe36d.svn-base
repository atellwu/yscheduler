#!/usr/bin/expect
cd /tmp/yscheduler
set AGENT_HOST [lindex $argv 0]
set USER [lindex $argv 1]
set PASSWORD [lindex $argv 2]
set DATE [lindex $argv 3]
set VERSION [lindex $argv 4]
set WEB_HOST [lindex $argv 5]
set timeout -1

   #scp
   spawn scp deploy/target/yagent-${VERSION}.zip $USER@${AGENT_HOST}:/dianyi/
   expect "password:" {
      send "${PASSWORD}\r"
   }
   expect "100%"
   #login
   spawn ssh $USER@$AGENT_HOST
   expect "(yes/no)?" {
         send "yes\r"
         expect "password:" {
             send "$PASSWORD\r"
         }
   } "password:" {
         send "$PASSWORD\r"
   }
   expect "*~*"
   #stop
   send "/dianyi/yagent/bin/jetty.sh stop\r"
   expect "*~*"
   #backup,unzip
   send "rm /dianyi/backup/yagent* -rf\r"
   expect "*~*"
   send "mv /dianyi/yagent /dianyi/backup/yagent.${DATE}\r"
   expect "*~*"
   send "unzip -q /dianyi/yagent-${VERSION}.zip -d /dianyi/\r"
   #start
   expect "*~*"
   send "/dianyi/yagent/bin/jetty.sh start\r"
   expect "*~*"

send "exit\r"
expect "*closed*"
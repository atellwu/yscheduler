## 相比azkaban

1. 支持agent管理，agent failover和负载均衡，以team为单位划分agent群组
2. 界面更友好，区分工作流和单任务，工作流创建方便
3. 支持私有和公共工作流（公共工作流用于部门的任务依赖），一个任务可出现在多个部门的工作流中，只运行一次;azkaban每个flow里的job都是单独运行。https://github.com/azkaban/azkaban/issues/409
4. 支持任务的自依赖配置（同个任务的多次运行可配置依赖关系，比如可配置一定要上次运行结束才继续/允许并行/允许跳过）
5. 支持http触发类型的任务
6. 支持cron表达式，更加灵活，azkaban是配置起始时间+周期

## 代码结构说明：
agent工程：agent模块
agent-framework工程：agent模块的基础框架，被agent模块依赖使用
common工程：整个项目的基础类
deploy工程：用于打包的工程
model工程：整个项目的类模型
monitor工程： 用于监控
scheduler工程： 调度模块，负责触发任务，实时跟踪任务的状态
storage工程： 用于存储附件
web工程：提供任务管理，权限管理等

## 部署架构图



## 项目依赖
单元测试代码依赖了yunit，可从这里下载到本地再install：https://github.com/atellwu/yunit

## 打包命令：
(1) 生成eclipse工程：  mvn eclipse:eclipse
(2) 打包： mvn clean package，最终会在deploy/target下生成相应的包，如：
   a.  yagent-0.6.1.zip   agent模块，用于部署到相应的负责执行的机器，解压后执行 bin/jetty.sh restart 即可启动
   b.  yscheduler-monitor-0.6.1.zip   monitor模块，负责通过监控数据库检测任务的执行是否正常，解压后执行 bin/start.sh 启动
   c.  yscheduler-storage-0.6.1.zip  附件存储模块，再创建Task时，界面可以上传附件，此模块就是用于存储附件的服务，是个war包，解压后放到jetty或tomcat的webapps目录即可。
   d.  yscheduler-web-0.6.1.zip web模块，是个war包，解压后放到jetty或tomcat的webapps目录即可。

## 安装部署：
1.agent模块部署：
  解压到指定目录：unzip yagent-$VERSION.zip -p /dianyi/app/
  启动：/dianyi/app/yagent/bin/jetty.sh start
2.web模块部署：
  解压到指定目录：unzip yscheduler-web-$VERSION.zip -d /dianyi/app/ue.yscheduler.dy/webapps/
    启动：/etc/init.d/resin restart
3.storage模块：
  解压到指定目录：unzip yagent-$VERSION.zip -p /dianyi/app/
  启动：/dianyi/app/yagent/bin/jetty.sh start


## 添加一台agent的步骤：
1. 在agent管理菜单里，添加agent，输入“名称” “ip” “分组”
2. 让agent所在的业务方，讲agent包下载后部署到agent机器上，启动起来。
    下载地址：http://qa.yscheduler.dy/download/agent.zip
    部署方法： 
      (1)解压到指定目录：unzip agent.zip -p /dianyi/app/
      (2)启动：/dianyi/app/yagent/bin/jetty.sh start

## 管理员升级agent机器的方法：
当agent有代码更新后，需要让已经安装agent的机器升级，可以使用此方法。
1. 上传agent.zip包
2. 上次agent升级脚本（该脚本在源代码中有，叫做upgrade.sh），之前已经上传过了，所以这一步一般可以不做，除非有修改upgrade.sh脚本
3. 在agent管理页面，每个agent都有对应的有个“升级”按钮，点击后，可进入升级页面。升级时，是每个agent单独升级，无法批量全部agent一起升级。

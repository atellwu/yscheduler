##相比azkaban

1. 支持agent管理，agent failover和负载均衡，以team为单位划分agent群组
2. 界面更友好，区分工作流和单任务，工作流创建方便
3. 支持私有和公共工作流（公共工作流用于部门的任务依赖），一个任务可出现在多个部门的工作流中，只运行一次;azkaban每个flow里的job都是单独运行。https://github.com/azkaban/azkaban/issues/409
4. 支持任务的自依赖配置（同个任务的多次运行可配置依赖关系，比如可配置一定要上次运行结束才继续/允许并行/允许跳过）
5. 支持http触发类型的任务
6. 支持cron表达式，更加灵活，azkaban是配置起始时间+周期


## 项目依赖
单元测试代码依赖了yunit，可从这里下载到本地再install：https://github.com/atellwu/yunit

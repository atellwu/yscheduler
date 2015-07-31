// package com.yeahmobi.yscheduler.web.controller.topo;
//
// import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
//
// public enum NodeStatus {
//
// DEPENDENCY_WAIT(1, "检查依赖"), //
// READY(10, "待运行"), //
// RUNNING(20, "运行中"), //
// SUCCESS(30, "运行成功"), //
// FAILED(40, "运行失败"), //
// WORKFLOW_FAILED(50, "工作流失败"), //
// CANCELLED(60, "取消运行"), //
// SKIPPED(70, "被跳过"), //
// COMPLETE_WITH_UNKNOWN_STATUS(80, "未知的结束状态");
//
// private int id;
// private String desc;
//
// NodeStatus(int id, String desc) {
// this.id = id;
// this.desc = desc;
// }
//
// public int getId() {
// return this.id;
// }
//
// public void setId(int id) {
// this.id = id;
// }
//
// public String getDesc() {
// return this.desc;
// }
//
// public void setDesc(String desc) {
// this.desc = desc;
// }
//
// public NodeStatus fromTaskInstanceStatus(TaskInstanceStatus status) {
// switch (status) {
// case TaskInstanceStatus
// }
// }
// }

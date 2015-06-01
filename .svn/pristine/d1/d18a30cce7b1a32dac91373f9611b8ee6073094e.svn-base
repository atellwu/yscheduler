package com.yeahmobi.yscheduler.model.type;

public enum WorkflowStatus {

    OPEN(1, "已开启"), PAUSED(2, "已暂停"), REMOVED(3, "已删除");

    private int    id;
    private String desc;

    WorkflowStatus(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static WorkflowStatus valueOf(int id) {
        switch (id) {
            case 1:
                return OPEN;
            case 2:
                return PAUSED;
            case 3:
                return REMOVED;
        }
        return null;
    }
}

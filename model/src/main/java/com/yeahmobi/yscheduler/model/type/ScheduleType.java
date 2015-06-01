package com.yeahmobi.yscheduler.model.type;

/**
 * @author Ryan Sun
 */
public enum ScheduleType {
    TASK(1, "任务"), //
    WORKFLOW(2, "工作流");

    private int    id;
    private String value;

    ScheduleType(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public int getId() {
        return this.id;
    }

    public static ScheduleType valueOf(int id) {
        switch (id) {
            case 1:
                return TASK;
            case 2:
                return WORKFLOW;
        }
        return null;
    }
}

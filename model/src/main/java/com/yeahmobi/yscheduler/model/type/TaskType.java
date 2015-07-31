package com.yeahmobi.yscheduler.model.type;

public enum TaskType {

    SHELL(1), HADOOP(10), HTTP(20);

    private int id;

    TaskType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name();
    }

    public static TaskType valueOf(int id) {
        switch (id) {
            case 1:
                return SHELL;
            case 10:
                return HADOOP;
            case 20:
                return HTTP;
        }
        return null;
    }

}

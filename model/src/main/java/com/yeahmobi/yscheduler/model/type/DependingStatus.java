package com.yeahmobi.yscheduler.model.type;

/**
 * @author Leo Liang
 */
public enum DependingStatus {

    NONE(1), //
    SUCCESS(10), //
    COMPLETED(20), //
    ;

    private int id;

    DependingStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name();
    }

    public static DependingStatus valueOf(int id) {
        switch (id) {
            case 1:
                return NONE;
            case 10:
                return SUCCESS;
            case 20:
                return COMPLETED;
        }
        return null;
    }

}

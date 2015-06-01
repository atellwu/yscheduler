package com.yeahmobi.yscheduler.model.type;

public enum AuthorityMode {

    NONE(0), READONLY(1), WRITABLE(2);

    private int id;

    AuthorityMode(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name();
    }

    public void setId(int id) {
        this.id = id;
    }

    public static AuthorityMode valueOf(int id) {
        switch (id) {
            case 0:
                return NONE;
            case 1:
                return READONLY;
            case 2:
                return WRITABLE;
        }
        return null;
    }

}

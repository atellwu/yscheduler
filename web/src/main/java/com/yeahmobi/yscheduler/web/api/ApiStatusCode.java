package com.yeahmobi.yscheduler.web.api;

/**
 * @author Leo Liang
 */
public enum ApiStatusCode {
    SUCCESS(0), //

    AUTH_FAILED(300), //
    BIZ_ERROR(400), //

    UNKNOWN_EXCEPTION(500), //

    ;

    private int code;

    private ApiStatusCode(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}

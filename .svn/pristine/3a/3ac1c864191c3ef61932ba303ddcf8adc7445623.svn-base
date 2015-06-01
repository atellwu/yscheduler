package com.yeahmobi.yscheduler.common.log;

import java.io.IOException;

import com.yeahmobi.yscheduler.common.HmacSha1Util;

public class AgentLogUtils {

    public static final char ENDLINE_SPLIT = '#';

    public static String getEndline(long attemptId) throws IOException {
        return HmacSha1Util.hmacSha1(String.valueOf(attemptId));
    }
}

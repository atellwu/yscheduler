package com.yeahmobi.yscheduler.common.log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface LogFileService {

    public OutputStream getOutputStream(long attemptId) throws IOException;

    public InputStream getInputStream(long attemptId) throws IOException;
}

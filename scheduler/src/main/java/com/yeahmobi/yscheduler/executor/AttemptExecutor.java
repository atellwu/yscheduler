package com.yeahmobi.yscheduler.executor;

import com.yeahmobi.yscheduler.model.Attempt;

/**
 * @author atell
 */
public interface AttemptExecutor {

    void submit(Attempt attempt);

    boolean isRunning(long instanceId);

    void cancel(long instanceId);

}

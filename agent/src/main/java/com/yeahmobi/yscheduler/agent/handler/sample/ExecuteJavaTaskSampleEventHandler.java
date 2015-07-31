package com.yeahmobi.yscheduler.agent.handler.sample;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.yeahmobi.yscheduler.agentframework.agent.event.TaskSubmitionEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.task.AbstractAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.BaseTaskExecutor;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskExecutor;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction;

/**
 * @author Leo.Liang
 */
public class ExecuteJavaTaskSampleEventHandler extends TaskSubmitionEventHandler {

    public static final String EVENT_TYPE = "javaSample";

    @Override
    public AgentTask getTask(Map<String, String> params) {
        String timesStr = params.get("times");
        Validate.isTrue(StringUtils.isNumeric(timesStr), "Parameter times invalid.");

        return new ExecuteJavaTaskSampleAgentTask(Integer.valueOf(timesStr), params);
    }

    private static final class ExecuteJavaTaskSampleAgentTask extends AbstractAgentTask {

        private int times;

        public ExecuteJavaTaskSampleAgentTask(int times, Map<String, String> params) {
            super(EVENT_TYPE, params);
            this.times = times;
        }

        public TaskExecutor getTaskExecutor() {
            return new ExecuteJavaTaskSampleTaskExecutor(this.times);
        }

    }

    private static final class ExecuteJavaTaskSampleTaskExecutor extends BaseTaskExecutor {

        private volatile boolean cancel  = false;
        private int              times;
        private final String     padding = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

        public ExecuteJavaTaskSampleTaskExecutor(int times) {
            this.times = times;
        }

        public Integer execute(TaskTransaction taskTransaction) throws InterruptedException {
            int i = 0;

            while (!this.cancel && (i < this.times)) {
                String log = String.valueOf(i++) + " ";
                for (int j = 0; j < 10; j++) {
                    log += this.padding;
                }
                taskTransaction.info(log);
                Thread.sleep(10);
            }

            return null;
        }

        public void cancel(TaskTransaction taskTransaction) throws Exception {
            this.cancel = true;
        }

    }

}

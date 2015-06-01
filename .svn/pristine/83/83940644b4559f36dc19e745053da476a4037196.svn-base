package com.yeahmobi.yscheduler.agentframework.agent.event.shell;

import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.event.TaskSubmitionEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.shell.ShellAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.shell.ShellTaskExecutor;

/**
 * @author Leo.Liang
 */
public class ShellTaskSubmitionEventHandler extends TaskSubmitionEventHandler {

    public static final String EVENT_TYPE = "SHELL_TASK_SUBMIT";

    private ShellTaskExecutor  taskExecutor;

    public void setShellTaskExecutor(ShellTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public AgentTask getTask(Map<String, String> params) {
        return new ShellAgentTask(EVENT_TYPE, params, this.taskExecutor);
    }

}

package com.yeahmobi.yscheduler.agent.handler;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.task.AbstractAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskExecutor;

public class JavaAgentTask extends AbstractAgentTask {

    private Map<String, String>              params;
    private Class<? extends JavaTaskHandler> taskHandlerClazz;

    public JavaAgentTask(String eventType, Map<String, String> params, Class<? extends JavaTaskHandler> taskHandlerClazz) {
        super(eventType, params);
        this.params = params;
        this.taskHandlerClazz = taskHandlerClazz;
    }

    public TaskExecutor getTaskExecutor() {
        try {
            Constructor<? extends JavaTaskHandler> constructor = this.taskHandlerClazz.getConstructor(Map.class);
            return constructor.newInstance(this.params);
        } catch (Exception e) {
            return null;
        }
    }

}

package com.yeahmobi.yscheduler.agent.handler;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.yeahmobi.yscheduler.agentframework.agent.event.TaskSubmitionEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;
import com.yeahmobi.yscheduler.common.Constants;

public class JavaTaskSubmitionEventHandler extends TaskSubmitionEventHandler {

    public static final String  EVENT_TYPE = "JAVA_TASK";

    private Map<String, String> executorMapping;

    /**
     * @param executorMapping the executorMapping to set
     */
    public void setExecutorMapping(Map<String, String> executorMapping) {
        this.executorMapping = executorMapping;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AgentTask getTask(Map<String, String> params) {
        String handlerType = params.get(Constants.PARAM_JAVA_TASK_HANDLER_TYPE);

        Validate.isTrue(StringUtils.isNotBlank(handlerType),
                        String.format("Parameter %s can not be empty or null.", Constants.PARAM_JAVA_TASK_HANDLER_TYPE));

        Validate.isTrue((this.executorMapping != null) && this.executorMapping.containsKey(handlerType),
                        String.format("No Java task handler found for type %s", handlerType));

        String handlerClazzStr = this.executorMapping.get(handlerType);

        Class handlerClazz;
        try {
            handlerClazz = Class.forName(handlerClazzStr);

            Validate.isTrue(JavaTaskHandler.class.isAssignableFrom(handlerClazz),
                            String.format("Java task handler class %s for type %s doesn't implement com.yeahmobi.yscheduler.agent.handler.JavaTaskExecutor interface.",
                                          handlerClazzStr, handlerType));

            handlerClazz.getConstructor(Map.class);

            return new JavaAgentTask(EVENT_TYPE, params, handlerClazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}

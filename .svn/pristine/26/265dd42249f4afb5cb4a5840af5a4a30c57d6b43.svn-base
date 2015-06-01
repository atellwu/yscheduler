package com.yeahmobi.yscheduler.common.variable;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class VariableManager {

    private static final String          VARIABLE_START   = "${";

    private static final String          VARIABLE_END     = "}";

    static final String                  VARIABLE_SPLIT   = ":";

    private Map<String, VariableHandler> variableHandlers = new HashMap<String, VariableHandler>();

    public void setVariableHandlers(Map<String, VariableHandler> variableHandlers) {
        this.variableHandlers = variableHandlers;
    }

    public String process(String command, VariableContext context) throws VariableException {
        if (StringUtils.isBlank(command)) {
            return command;
        }
        StringBuilder commandBuilder = new StringBuilder();
        int length = command.length(), startPosition = 0;
        boolean done = false;

        do {
            done = true;
            int variableStart = command.indexOf(VARIABLE_START, startPosition);
            if (variableStart >= 0) {
                // 拼接前部分
                commandBuilder.append(command.substring(startPosition, variableStart));

                int variableEnd = command.indexOf(VARIABLE_END, variableStart);
                if (variableEnd >= 0) {
                    done = false;

                    String variableName = command.substring(variableStart + VARIABLE_START.length(), variableEnd);
                    String variableValue = handle(context, variableName);

                    // 拼接变量值
                    commandBuilder.append(variableValue);

                    // 继续找下一个
                    startPosition = variableEnd + 1;
                } else {
                    // 拼接到结束
                    commandBuilder.append(command.substring(variableStart));
                }
            } else {
                // 拼接到结束
                commandBuilder.append(command.substring(startPosition));
            }
        } while (!done && (startPosition < length));

        return commandBuilder.toString();
    }

    private String handle(VariableContext context, String variable) throws VariableException {
        String variableValue = VARIABLE_START + variable + VARIABLE_END;// 默认是保留原样

        String variableName = variable;
        String variableParam = "";
        int split = variable.indexOf(VARIABLE_SPLIT);
        if (split >= 0) {
            variableName = variable.substring(0, split);
            if (split < variable.length()) {
                variableParam = variable.substring(split + 1);
            }
        }
        VariableHandler handler = this.variableHandlers.get(variableName);
        if (handler != null) {
            String[] params = StringUtils.splitPreserveAllTokens(variableParam, ':');
            variableValue = handler.process(params, context);
        }

        return variableValue;
    }

}

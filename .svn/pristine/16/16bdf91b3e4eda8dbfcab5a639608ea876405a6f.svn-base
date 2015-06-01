package com.yeahmobi.yscheduler.agentframework.agent.event.task;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;

import com.yeahmobi.yscheduler.agentframework.agent.event.HandlerResult;
import com.yeahmobi.yscheduler.agentframework.agent.event.TaskExecutionEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.event.task.CalloutTaskHolder.Pair;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction;

/**
 * @author atell.wu
 */
public class CallbackEventHandler extends TaskExecutionEventHandler {

    public static final String EVENT_TYPE = "TASK_CALLBACK";

    public void onEvent(Map<String, String> params, HandlerResult handlerResult) {

        validate(params);

        Long txId = Long.valueOf(params.get(PARAM_TX_ID));
        Integer returnValue = Integer.valueOf(params.get("returnValue"));
        String log = params.get("log");

        // 取回callout的pair
        Pair pair = CalloutTaskHolder.get(txId);
        Validate.notNull(pair, String.format("Params invalid (txId '%s' is not match a callout)", txId));
        TaskTransaction calloutTx = pair.getTx();

        calloutTx.info("Callback came, params: " + params);

        // 存储log
        if (StringUtils.isNotEmpty(log)) {
            calloutTx.info(log);
        }

        // 传递returnValue给 futureTask
        pair.task.set(returnValue);
        pair.task.run();

    }

    private void validate(Map<String, String> params) throws IllegalArgumentException {
        String returnValue = params.get("returnValue");
        Validate.notEmpty(returnValue, "returnValue cannot be empty");
        Validate.isTrue(NumberUtils.isNumber(returnValue), "returnValue should be numberic");
        String txId = params.get("txId");
        Validate.isTrue(NumberUtils.isNumber(txId), "Params invalid (txId is invalid)");
    }

}

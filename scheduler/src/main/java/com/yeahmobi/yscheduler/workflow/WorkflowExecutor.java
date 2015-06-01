package com.yeahmobi.yscheduler.workflow;

import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;

/**
 * @author Ryan Sun
 */
public interface WorkflowExecutor {

    public void submit(Workflow workflow, WorkflowInstance workflowInstance);

    public void restore(WorkflowInstance workflowInstance);

    public void cancel(long workflowInstanceId);

}

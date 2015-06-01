package com.yeahmobi.yscheduler.workflow;

import java.util.List;

import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;

/**
 * @author Leo Liang
 */
public interface WorkflowEngine {

    public void cancel(long workflowInstanceId);

    public void submit(Workflow workflow, WorkflowInstance workflowInstance, List<TaskInstance> taskIntances);

}

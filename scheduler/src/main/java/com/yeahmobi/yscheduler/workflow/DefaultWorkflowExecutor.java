package com.yeahmobi.yscheduler.workflow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;

/**
 * @author Ryan Sun
 */
@Service
public class DefaultWorkflowExecutor implements WorkflowExecutor {

    @Autowired
    private WorkflowService         workflowService;

    @Autowired
    private WorkflowDetailService   workflowDetailService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private TaskInstanceService     taskInstanceService;

    @Autowired
    @Qualifier("private")
    private WorkflowEngine          privateWorkflowEngine;

    @Autowired
    @Qualifier("common")
    private WorkflowEngine          commonWorkflowEngine;

    @PostConstruct
    public void init() {
        List<WorkflowInstance> instances = this.workflowInstanceService.getAllInits();
        for (WorkflowInstance instance : instances) {
            submit(this.workflowService.get(instance.getWorkflowId()), instance);
        }
    }

    public void submit(Workflow workflow, WorkflowInstance workflowInstance) {
        long workflowId = workflowInstance.getWorkflowId();
        long instanceId = workflowInstance.getId();
        List<WorkflowDetail> details = this.workflowDetailService.list(workflowId);
        List<TaskInstance> taskInstances = new ArrayList<TaskInstance>();
        for (WorkflowDetail detail : details) {
            long taskId = detail.getTaskId();
            TaskInstance instance = this.taskInstanceService.get(taskId, instanceId);
            if (instance == null) {
                instance = new TaskInstance();
                instance.setTaskId(taskId);
                instance.setWorkflowInstanceId(instanceId);
                instance.setStatus(TaskInstanceStatus.DEPENDENCY_WAIT);
                this.taskInstanceService.save(instance);
            } else if (instance.getStatus() != TaskInstanceStatus.SUCCESS) {
                this.taskInstanceService.updateStatus(instance.getId(), TaskInstanceStatus.DEPENDENCY_WAIT);
            }

            taskInstances.add(instance);
        }
        if ((workflow.getCommon() != null) && workflow.getCommon()) {
            this.commonWorkflowEngine.submit(workflow, workflowInstance, taskInstances);
        } else {
            this.privateWorkflowEngine.submit(workflow, workflowInstance, taskInstances);
        }

    }

    public void restore(WorkflowInstance workflowInstance) {
        long workflowInstanceId = workflowInstance.getId();
        if (!workflowInstance.getStatus().isCompleted()) {
            throw new IllegalStateException("工作流仍在运行中，不能重跑");
        } else if (workflowInstance.getStatus() == WorkflowInstanceStatus.SUCCESS) {
            throw new IllegalStateException("工作流已经成功，不能重跑");
        } else {
            this.workflowInstanceService.updateStatus(workflowInstanceId, WorkflowInstanceStatus.INITED);
            submit(this.workflowService.get(workflowInstance.getWorkflowId()), workflowInstance);
        }
    }

    public void cancel(long workflowInstanceId) {
        WorkflowInstance workflowInstance = this.workflowInstanceService.get(workflowInstanceId);
        if ((workflowInstance != null) && (workflowInstance.getWorkflowId() != null)) {
            Workflow workflow = this.workflowService.get(workflowInstance.getWorkflowId());
            if (workflow != null) {
                if ((workflow.getCommon() != null) && workflow.getCommon()) {
                    this.commonWorkflowEngine.cancel(workflowInstanceId);
                } else {
                    this.privateWorkflowEngine.cancel(workflowInstanceId);
                }
            }
        }

    }

}

package com.yeahmobi.yscheduler.workflow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;

/**
 * @author Leo Liang
 */
@Service("private")
public class PrivateWorkflowEngine extends AbstractWorkflowEngine {

    private static final long CHECK_INTERVAL = 1000 * 5;

    @Override
    protected List<WorkflowInstance> getAllRunningWorkflowInstances() {
        return this.workflowInstanceService.getAllRunning(false);
    }

    @Override
    protected String getName() {
        return "PrivateWorkflowEngine";
    }

    @Override
    protected long getCheckIntervalMilliseconds() {
        return CHECK_INTERVAL;
    }

    @Override
    protected void checkAndExecute(Map.Entry<Long, Pair> runningWorkflowEntry) {

        Long workflowInstanceId = runningWorkflowEntry.getKey();
        Workflow workflow = runningWorkflowEntry.getValue().workflow;
        Map<Long, TaskInstanceWithDependency> taskInstances = runningWorkflowEntry.getValue().taskInstances;
        WorkflowInstance workflowInstance = PrivateWorkflowEngine.this.workflowInstanceService.get(workflowInstanceId);
        if (workflowInstance == null) {
            return;
        }

        Collection<TaskInstanceWithDependency> taskInstanceWithDependencies = taskInstances.values();

        fetchLatestStatus(taskInstanceWithDependencies);

        boolean cancelled = handleCancelIfNeeded(workflowInstanceId, taskInstanceWithDependencies);

        // 判断工作流是否结束
        boolean allTasksSuccess = isAllTasksSuccess(workflowInstanceId, taskInstanceWithDependencies);

        if (allTasksSuccess) {
            workflowSuccess(workflowInstanceId);
            return;
        }

        boolean hasFailTask = hasFailTask(workflowInstanceId, taskInstanceWithDependencies);

        if (hasFailTask) {
            if (isAllTasksCompleted(workflowInstanceId, taskInstanceWithDependencies)) {
                workflowFail(workflowInstanceId, taskInstanceWithDependencies);
            } else {
                setAllDependencyWaitTasksAsWorkflowFail(workflowInstanceId, taskInstanceWithDependencies);
                cancelAllRunningTasks(workflowInstanceId, taskInstanceWithDependencies);
            }
            return;
        }

        boolean hasCancelledTaskAndNoUncompleted = hasCancelledTaskAndNoUncompleted(workflowInstanceId,
                                                                                    taskInstanceWithDependencies);

        if (hasCancelledTaskAndNoUncompleted) {
            workflowCancel(workflowInstanceId);
            return;
        }

        if (!cancelled) {
            // 超时只是报警，无做其他处理，故在loop里每次检查一下即可
            checkTimeout(workflow, workflowInstance);

            // 判断依赖是否满足
            for (TaskInstanceWithDependency taskInstanceWd : taskInstances.values()) {
                submitToTaskInstanceExecutorIfConditionSatisfy(workflow, workflowInstance,
                                                               taskInstanceWithDependencies, taskInstanceWd);
            }
        }

    }
}

package com.yeahmobi.yscheduler.model.common;

import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.TaskType;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;

public class Query {

    private TaskType               taskType;

    private TaskStatus             taskStatus;
    private WorkflowStatus         workflowStatus;

    private TaskInstanceStatus     taskInstanceStatus;
    private WorkflowInstanceStatus workflowInstanceStatus;

    private TaskScheduleType       taskScheduleType;
    private WorkflowScheduleType   workflowScheduleType;

    private String                 name;
    private Long                   owner;

    public TaskType getTaskType() {
        return this.taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskStatus getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public WorkflowStatus getWorkflowStatus() {
        return this.workflowStatus;
    }

    public void setWorkflowStatus(WorkflowStatus workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return this.owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public TaskInstanceStatus getTaskInstanceStatus() {
        return this.taskInstanceStatus;
    }

    public void setTaskInstanceStatus(TaskInstanceStatus taskInstanceStatus) {
        this.taskInstanceStatus = taskInstanceStatus;
    }

    public WorkflowInstanceStatus getWorkflowInstanceStatus() {
        return this.workflowInstanceStatus;
    }

    public void setWorkflowInstanceStatus(WorkflowInstanceStatus workflowInstanceStatus) {
        this.workflowInstanceStatus = workflowInstanceStatus;
    }

    public TaskScheduleType getTaskScheduleType() {
        return this.taskScheduleType;
    }

    public void setTaskScheduleType(TaskScheduleType taskScheduleType) {
        this.taskScheduleType = taskScheduleType;
    }

    public WorkflowScheduleType getWorkflowScheduleType() {
        return this.workflowScheduleType;
    }

    public void setWorkflowScheduleType(WorkflowScheduleType workflowScheduleType) {
        this.workflowScheduleType = workflowScheduleType;
    }

    public String getQueryString() {
        StringBuilder builder = new StringBuilder();
        if (this.taskType != null) {
            builder.append("taskType=").append(this.taskType.getId()).append('&');
        }
        if (this.taskStatus != null) {
            builder.append("taskStatus=").append(this.taskStatus.getId()).append('&');
        }
        if (this.workflowStatus != null) {
            builder.append("workflowStatus=").append(this.workflowStatus.getId()).append('&');
        }
        if (this.taskInstanceStatus != null) {
            builder.append("taskInstanceStatus=").append(this.taskInstanceStatus.getId()).append('&');
        }
        if (this.workflowInstanceStatus != null) {
            builder.append("workflowInstanceStatus=").append(this.workflowInstanceStatus.getId()).append('&');
        }
        if (this.name != null) {
            builder.append("name=").append(this.name).append('&');
        }
        if (this.owner != null) {
            builder.append("owner=").append(this.owner).append('&');
        }
        if (this.taskScheduleType != null) {
            builder.append("taskScheduleType=").append(this.taskScheduleType.getId()).append('&');
        }
        if (this.workflowScheduleType != null) {
            builder.append("workflowScheduleType=").append(this.workflowScheduleType.getId()).append('&');
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    public static enum TaskScheduleType {
        AUTO(1, "自动执行"), MANAUAL(2, "手动执行"), WORKFLOW_SCHEDULED(3, "工作流触发");

        private int    id;
        private String desc;

        TaskScheduleType(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static TaskScheduleType valueOf(int id) {
            switch (id) {
                case 1:
                    return AUTO;
                case 2:
                    return MANAUAL;
                case 3:
                    return WORKFLOW_SCHEDULED;
            }
            return null;
        }
    }

    public static enum WorkflowScheduleType {
        AUTO(1, "自动执行"), MANAUAL(2, "手动执行");

        private int    id;
        private String desc;

        WorkflowScheduleType(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static WorkflowScheduleType valueOf(int id) {
            switch (id) {
                case 1:
                    return AUTO;
                case 2:
                    return MANAUAL;
            }
            return null;
        }
    }

}

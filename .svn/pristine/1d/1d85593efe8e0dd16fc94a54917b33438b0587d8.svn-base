package com.yeahmobi.yscheduler.agentframework.agent.task.shell;

import java.util.Map;

import org.apache.commons.lang.Validate;

import com.yeahmobi.yscheduler.agentframework.agent.task.AbstractAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskExecutor;

/**
 * @author Leo.Liang
 */
public class ShellAgentTask extends AbstractAgentTask {

    private static final String PARAM_ATTACHMENT_VERSION    = "attachmentVersion";
    private static final String PARAM_ATTACHMENT_SERVER_URI = "attachmentServerUri";
    private static final String PARAM_SHELL_COMMAND         = "shellCmd";

    private String              command;
    private String              attachmentServerUri;
    private Long                attachmentVersion;

    private TaskExecutor        taskExecutor;

    public ShellAgentTask(String eventType, Map<String, String> params, TaskExecutor taskExecutor) {
        super(eventType, params);
        // command
        String command = params.get(PARAM_SHELL_COMMAND);
        Validate.notEmpty(command, "Parameter shellCmd can not be empty or null.");
        this.command = command;
        // attachmentVersion
        String attachmentVersionStr = params.get(PARAM_ATTACHMENT_VERSION);
        if (attachmentVersionStr != null) {
            this.attachmentVersion = Long.parseLong(attachmentVersionStr);
            // attachmentServerUri TODO web把attachmentServerUri也传递过来
            this.attachmentServerUri = params.get(PARAM_ATTACHMENT_SERVER_URI);
            Validate.notEmpty(this.attachmentServerUri,
                              "Parameter attachmentServerUri can not be empty or null with attachmentVersion exists.");
        }

        this.taskExecutor = taskExecutor;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getAttachmentVersion() {
        return this.attachmentVersion;
    }

    public String getAttachmentServerUri() {
        return this.attachmentServerUri;
    }

    public TaskExecutor getTaskExecutor() {
        return this.taskExecutor;
    }

    @Override
    public String toString() {
        return "ShellAgentTask [command=" + this.command + ", attachmentVersion=" + this.attachmentVersion
               + ", taskExecutor=" + this.taskExecutor + "]";
    }

}

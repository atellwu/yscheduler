package com.yeahmobi.yscheduler.web.controller.topo;

import java.util.List;

import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;

public class TopoNode {

    /** task name */
    private String             name;

    private int                row;

    private int                column;

    private String             url;

    private List<TopoNode>     nodes;

    private long               taskId;

    private boolean            hasArrived;

    private TaskInstanceStatus status     = null;

    private Long               teamId;

    private boolean            dottedLine = false;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
        this.hasArrived = true;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TopoNode> getNodes() {
        return this.nodes;
    }

    public void setNodes(List<TopoNode> nodes) {
        this.nodes = nodes;
    }

    public TaskInstanceStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskInstanceStatus status) {
        this.status = status;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public boolean getDottedLine() {
        return this.dottedLine;
    }

    public void setDottedLine(boolean dottedLine) {
        this.dottedLine = dottedLine;
    }

    public boolean getHasArrived() {
        return this.hasArrived;
    }

    public void setHasArrived(boolean hasArrived) {
        this.hasArrived = hasArrived;
    }

    @Override
    public String toString() {
        return "TopoNode [name=" + this.name + ", row=" + this.row + ", column=" + this.column + ", url=" + this.url
               + ", nodes=" + this.nodes + ", taskId=" + this.taskId + ", hasArrived=" + this.hasArrived + ", status="
               + this.status + ", teamId=" + this.teamId + ", dottedLine=" + this.dottedLine + "]";
    }

}

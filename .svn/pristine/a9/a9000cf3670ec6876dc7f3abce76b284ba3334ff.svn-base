package com.yeahmobi.yscheduler.web.topo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.web.controller.topo.TopoNode;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

@Service("privateTopo")
public class PrivateWorkflowTopoService implements WorkflowTopoService {

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private WorkflowDetailService   detailService;

    @Autowired
    private TaskService             taskService;

    @Autowired
    private TaskInstanceService     taskInstanceService;

    private List<WorkflowDetailVO> getWorkflowDetail(long workflowId) {
        List<WorkflowDetailVO> raw = new ArrayList<WorkflowDetailVO>();
        List<WorkflowDetail> details = this.detailService.list(workflowId);

        for (WorkflowDetail detail : details) {
            WorkflowDetailVO detailVO = new WorkflowDetailVO();
            Long taskId = detail.getTaskId();
            List<Long> dependencies = this.detailService.listDependencyTaskIds(workflowId, taskId);
            detailVO.setWorkflowDetail(detail);
            detailVO.setDependencies(dependencies);
            detailVO.setTaskName(this.taskService.get(taskId).getName());
            raw.add(detailVO);
        }
        return raw;
    }

    private TopoNode buildTree(List<WorkflowDetailVO> raw) {
        Map<Long, TopoNode> nodes = new HashMap<Long, TopoNode>();
        Set<TopoNode> rootNodes = new HashSet<TopoNode>();// 用于求root（有被依赖的节点的集合，没被依赖的则是root）
        for (WorkflowDetailVO detail : raw) {
            Long taskId = detail.getWorkflowDetail().getTaskId();
            TopoNode node = new TopoNode();
            node.setName(detail.getTaskName());
            node.setTaskId(taskId);
            nodes.put(taskId, node);
            rootNodes.add(node);
        }
        for (WorkflowDetailVO detail : raw) {
            TopoNode node = nodes.get(detail.getWorkflowDetail().getTaskId());
            List<TopoNode> dependNodes = new ArrayList<TopoNode>();
            node.setNodes(dependNodes);
            List<Long> dependencies = detail.getDependencies();
            for (Long taskId : dependencies) {
                dependNodes.add(nodes.get(taskId));
            }
            rootNodes.removeAll(dependNodes);
        }

        if (rootNodes.size() <= 0) {
            throw new IllegalStateException("Can not found root node, details=" + raw);
        }

        return rootNodes.iterator().next();
    }

    private void setStatus(TopoNode root, long workflowInstanceId) {
        List<TaskInstance> instances = this.taskInstanceService.listByWorkflowInstanceId(workflowInstanceId);
        for (TaskInstance instance : instances) {
            Map<Long, TopoNode> map = new HashMap<Long, TopoNode>();
            treeToMap(root, map);
            long taskId = instance.getTaskId();
            TopoNode node = map.get(taskId);
            if (node != null) {
                node.setStatus(instance.getStatus());
            }
        }
    }

    private void treeToMap(TopoNode root, Map<Long, TopoNode> map) {
        map.put(root.getTaskId(), root);
        List<TopoNode> children = root.getNodes();
        if (children != null) {
            for (TopoNode child : children) {
                treeToMap(child, map);
            }
        }
    }

    public TopoNode buildWorkflowTopoTree(Long workflowId, List<WorkflowDetailVO> raw, boolean isAdmin, long userId) {
        return buildTree(raw);
    }

    public TopoNode buildWorkflowTopoTree(long workflowId, boolean isAdmin, long userId) {
        List<WorkflowDetailVO> raw = getWorkflowDetail(workflowId);
        return buildWorkflowTopoTree(workflowId, raw, isAdmin, userId);
    }

    public TopoNode buildInstanceTopoTree(long workflowInstanceId, boolean isAdmin, long userId) {
        WorkflowInstance workflowInstance = this.workflowInstanceService.get(workflowInstanceId);
        List<WorkflowDetailVO> raw = getWorkflowDetail(workflowInstance.getWorkflowId());
        TopoNode root = buildTree(raw);
        setStatus(root, workflowInstanceId);
        return root;
    }
}

package com.yeahmobi.yscheduler.web.topo.service;

import java.util.List;

import com.yeahmobi.yscheduler.web.controller.topo.TopoNode;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

public interface WorkflowTopoService {

    TopoNode buildWorkflowTopoTree(Long workflowId, List<WorkflowDetailVO> raw, boolean isAdmin, long userId);

    TopoNode buildWorkflowTopoTree(long workflowId, boolean isAdmin, long userId);

    TopoNode buildInstanceTopoTree(long workflowInstanceId, boolean isAdmin, long userId);
}

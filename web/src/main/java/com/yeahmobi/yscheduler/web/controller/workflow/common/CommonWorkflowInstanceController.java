package com.yeahmobi.yscheduler.web.controller.workflow.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.common.Query.WorkflowScheduleType;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.TeamWorkflowStatusInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.workflow.WorkflowExecutor;

/**
 * @author Leo.Liang
 */
@Controller
@RequestMapping(value = { CommonWorkflowInstanceController.SCREEN_NAME })
public class CommonWorkflowInstanceController extends AbstractController {

    public static final String                SCREEN_NAME = "common/instance";

    @Autowired
    private WorkflowService                   workflowService;

    @Autowired
    private WorkflowInstanceService           instanceService;

    @Autowired
    private TeamWorkflowStatusInstanceService teamInstanceService;

    @Autowired
    private WorkflowExecutor                  workflowExecutor;

    @RequestMapping(value = { "" })
    public ModelAndView index(Integer workflowInstanceStatus, Integer workflowScheduleType, Integer pageNum,
                              long workflowId) {
        Map<String, Object> map = new HashMap<String, Object>();

        Query query = buildQuery(map, workflowInstanceStatus, workflowScheduleType);

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        int successCount = 0;
        int totalRunCount = 0;

        // 处理状态(对于普通用户，状态应该由本team的所有运行结果来决定)
        if (!UserContextHolder.getUserContext().isAdmin()) {
            List<TeamWorkflowInstanceStatus> teamInstances = this.teamInstanceService.list(query,
                                                                                           UserContextHolder.getUserContext().getId(),
                                                                                           workflowId, pageNum,
                                                                                           paginator);
            if (teamInstances.size() > 0) {
                List<WorkflowInstance> instances = this.instanceService.list(getIds(teamInstances));
                deelStatus(teamInstances, instances);
                map.put("list", instances);
            }

            List<TeamWorkflowInstanceStatus> allTeamInstances = this.teamInstanceService.list(query,
                                                                                              UserContextHolder.getUserContext().getId(),
                                                                                              workflowId, pageNum,
                                                                                              paginator);
            for (TeamWorkflowInstanceStatus workflowInstance : allTeamInstances) {
                if (workflowInstance.getStatus().isCompleted()) {
                    totalRunCount++;
                    if (workflowInstance.getStatus() == WorkflowInstanceStatus.SUCCESS) {
                        successCount++;
                    }
                }
            }
        } else {
            List<WorkflowInstance> instances = this.instanceService.list(query, workflowId, pageNum, paginator);
            List<WorkflowInstance> allInstances = this.instanceService.listAll(workflowId);

            for (WorkflowInstance workflowInstance : allInstances) {
                if (workflowInstance.getStatus().isCompleted()) {
                    totalRunCount++;
                    if (workflowInstance.getStatus() == WorkflowInstanceStatus.SUCCESS) {
                        successCount++;
                    }
                }
            }

            map.put("list", instances);
        }

        map.put("workflow", this.workflowService.get(workflowId));
        map.put("paginator", paginator);
        map.put("successRate", (int) ((successCount * 100.00d) / totalRunCount));
        map.put("totalRunCount", totalRunCount);

        return screen(map, SCREEN_NAME);
    }

    private void deelStatus(List<TeamWorkflowInstanceStatus> teamInstances, List<WorkflowInstance> instances) {
        if (instances != null) {
            for (int i = 0; i < instances.size(); i++) {
                WorkflowInstance instance = instances.get(i);
                TeamWorkflowInstanceStatus teamInstance = teamInstances.get(i);
                instance.setStatus(teamInstance.getStatus());
            }
        }
    }

    private List<Long> getIds(List<TeamWorkflowInstanceStatus> teamInstances) {
        return new ArrayList<Long>(Collections2.transform(teamInstances,
                                                          new Function<TeamWorkflowInstanceStatus, Long>() {

                                                              public Long apply(TeamWorkflowInstanceStatus input) {
                                                                  return input.getWorkflowInstanceId();
                                                              }

                                                          }));
    }

    private Query buildQuery(Map<String, Object> map, Integer workflowInstanceStatus, Integer scheduleType) {
        Query query = new Query();

        if (workflowInstanceStatus != null) {
            WorkflowInstanceStatus status = WorkflowInstanceStatus.valueOf(workflowInstanceStatus);
            query.setWorkflowInstanceStatus(status);
        }

        if (scheduleType != null) {
            WorkflowScheduleType type = WorkflowScheduleType.valueOf(scheduleType);
            query.setWorkflowScheduleType(type);
        }

        map.put("query", query);
        map.put("allStatus", WorkflowInstanceStatus.values());
        map.put("scheduleTypes", WorkflowScheduleType.values());

        return query;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object cancel(HttpSession session, long workflowInstanceId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.workflowExecutor.cancel(workflowInstanceId);

            map.put("success", true);
        } catch (IllegalArgumentException e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        } catch (Exception e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        }
        return JSON.toJSONString(map);

    }

    @RequestMapping(value = "/rerun", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object rerun(long instanceId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.workflowExecutor.restore(this.instanceService.get(instanceId));
            map.put("success", true);
        } catch (IllegalArgumentException e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        } catch (Exception e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        }
        return JSON.toJSONString(map);

    }
}

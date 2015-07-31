package com.yeahmobi.yscheduler.web.controller.workflow.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.WorkflowVO;
import com.yeahmobi.yscheduler.workflow.WorkflowExecutor;

/**
 * @author Leo.Liang
 */
@Controller
@RequestMapping(value = { CommonWorkflowListController.SCREEN_NAME })
public class CommonWorkflowListController extends AbstractController {

    public static final String      SCREEN_NAME = "common";

    @Autowired
    private WorkflowService         workflowService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private UserService             userService;

    @Autowired
    private WorkflowExecutor        workflowExecutor;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<Workflow> workflows = this.workflowService.list(new Query(), pageNum, paginator,
                                                             UserContextHolder.getUserContext().getId(), true);
        List<WorkflowVO> list = new ArrayList<WorkflowVO>(workflows.size());
        if (workflows != null) {
            List<NameValuePair> userList = this.userService.list();

            HashMap<Long, String> userNameMapping = new HashMap<Long, String>();
            for (NameValuePair pair : userList) {
                userNameMapping.put(pair.getValue(), pair.getName());
            }

            for (Workflow workflow : workflows) {
                WorkflowVO vo = new WorkflowVO(workflow, userNameMapping.get(workflow.getOwner()));
                list.add(vo);
            }
        }

        map.put("list", list);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = { "" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object action(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        long workflowId = Long.parseLong(request.getParameter("id"));
        Workflow workflow = this.workflowService.get(workflowId);
        String action = request.getParameter("action");
        String actionChinese = request.getParameter("actionChinese");
        String notice = actionChinese + "工作流" + workflow.getName();
        String targetUrl = "/common";
        boolean success = false;
        try {
            if ("suspend".equals(action)) {
                workflow.setStatus(WorkflowStatus.PAUSED);
                this.workflowService.createOrUpdate(workflow);
            } else if ("resume".equals(action)) {
                workflow.setStatus(WorkflowStatus.OPEN);
                this.workflowService.createOrUpdate(workflow);
            } else if ("trigger".equals(action)) {
                WorkflowInstance instance = new WorkflowInstance();
                instance.setStatus(WorkflowInstanceStatus.INITED);
                instance.setWorkflowId(workflowId);
                // 创建instance，保存instance到数据库
                this.workflowInstanceService.save(instance);
                this.workflowExecutor.submit(workflow, instance);

                targetUrl = "/common/instance?workflowId=" + workflowId;
            } else if ("delete".equals(action)) {
                workflow.setStatus(WorkflowStatus.REMOVED);
                this.workflowService.createOrUpdate(workflow);
            }
            success = true;
            notice += "成功！";
        } catch (Exception e) {
            success = false;
            notice += "失败！";
            notice += e.getMessage();
        }

        Paginator paginator = new Paginator();
        List<Workflow> list = this.workflowService.list(new Query(), 0, paginator,
                                                        UserContextHolder.getUserContext().getId(), true);
        map.put("list", list);
        map.put("notice", notice);
        map.put("success", success);
        map.put("target", targetUrl);
        map.put("paginator", paginator);
        return JSON.toJSONString(map);
    }
}

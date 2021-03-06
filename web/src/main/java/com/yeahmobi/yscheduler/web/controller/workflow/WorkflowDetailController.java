package com.yeahmobi.yscheduler.web.controller.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

/**
 * @author Ryan Sun
 */
@Controller
@RequestMapping(value = { WorkflowDetailController.SCREEN_NAME })
public class WorkflowDetailController extends AbstractController {

    public static final String    SCREEN_NAME = "workflow/detail";

    @Autowired
    private TaskService           taskService;

    @Autowired
    private UserService           userService;

    @Autowired
    private WorkflowDetailService workflowDetailService;

    @Autowired
    private WorkflowService       workflowService;

    @RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable
    long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<NameValuePair> tasks = this.taskService.list(UserContextHolder.getUserContext().getId());
        Workflow workflow = this.workflowService.get(id);
        List<WorkflowDetail> details = this.workflowDetailService.list(workflow.getId());
        List<WorkflowDetailVO> list = new ArrayList<WorkflowDetailVO>();
        for (WorkflowDetail detail : details) {
            WorkflowDetailVO workflowVO = new WorkflowDetailVO();
            list.add(workflowVO);
            workflowVO.setDependencies(this.workflowDetailService.listDependencyTaskIds(detail.getWorkflowId(),
                                                                                        detail.getTaskId()));
            workflowVO.setWorkflowDetail(detail);
        }
        map.put("list", list);
        map.put("workflow", workflow);
        map.put("tasks", tasks);
        map.put("users", this.userService.list());
        map.put("canModify", this.workflowService.canModify(id, UserContextHolder.getUserContext().getId()));

        return screen(map, SCREEN_NAME);
    }
}

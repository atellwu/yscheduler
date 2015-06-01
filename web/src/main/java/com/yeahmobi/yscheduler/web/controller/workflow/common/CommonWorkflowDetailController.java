package com.yeahmobi.yscheduler.web.controller.workflow.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.model.Task;
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
@RequestMapping(value = { CommonWorkflowDetailController.SCREEN_NAME })
public class CommonWorkflowDetailController extends AbstractController {

    public static final String    SCREEN_NAME       = "common/detail";

    private static final String   ADMIN_SCREEN_NAME = "common/admin_detail";

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
        long userId = UserContextHolder.getUserContext().getId();
        List<NameValuePair> tasksForCurrentUser = null;
        List<NameValuePair> allTasks = this.taskService.list();
        Map<Long, NameValuePair> taskMap = new HashMap<Long, NameValuePair>();
        for (NameValuePair pair : allTasks) {
            taskMap.put(pair.getValue(), pair);
        }

        Workflow workflow = this.workflowService.get(id);
        Long workflowId = workflow.getId();
        List<WorkflowDetail> allDetails = this.workflowDetailService.list(workflowId);
        List<WorkflowDetail> detailsForCurrentUser = null;
        Set<NameValuePair> dependencies = new HashSet<NameValuePair>();

        Task rootTask = this.taskService.getRootTask("");
        Task teamTask = this.taskService.getRootTask(this.userService.getTeamName(userId));

        if (UserContextHolder.getUserContext().isAdmin()) {
            tasksForCurrentUser = allTasks;
            detailsForCurrentUser = allDetails;
        } else {
            tasksForCurrentUser = this.taskService.list(userId);
            detailsForCurrentUser = this.workflowDetailService.list(workflowId, userId);

            for (WorkflowDetail detail : this.workflowDetailService.listAllExceptRootTask(workflowId)) {
                Long taskId = detail.getTaskId();
                dependencies.add(taskMap.get(taskId));
            }
            map.put("delay", this.workflowDetailService.getTeamDelay(workflowId, userId));

        }
        dependencies.addAll(tasksForCurrentUser);

        List<WorkflowDetailVO> list = new ArrayList<WorkflowDetailVO>();

        for (WorkflowDetail detail : detailsForCurrentUser) {
            WorkflowDetailVO workflowVO = new WorkflowDetailVO();
            list.add(workflowVO);
            workflowVO.setDependencies(this.workflowDetailService.listDependencyTaskIds(detail.getWorkflowId(),
                                                                                        detail.getTaskId()));
            workflowVO.setWorkflowDetail(detail);
        }
        map.put("root", rootTask);
        map.put("team", teamTask);
        map.put("list", list);
        map.put("workflow", workflow);
        map.put("tasks", tasksForCurrentUser);
        map.put("dependencies", dependencies);
        map.put("users", this.userService.list());

        if (UserContextHolder.getUserContext().isAdmin()) {
            return screen(map, ADMIN_SCREEN_NAME);
        }
        return screen(map, SCREEN_NAME);
    }
}

package com.yeahmobi.yscheduler.web.controller.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.service.AttemptService;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;

/**
 * @author wukezhu
 */
@Controller
@RequestMapping(value = { TaskAttemptController.SCREEN_NAME })
public class TaskAttemptController extends AbstractController {

    public static final String  SCREEN_NAME = "task/instance/attempt";

    @Autowired
    private TaskService         taskService;

    @Autowired
    private TaskInstanceService instanceService;

    @Autowired
    private AttemptService      attemptService;

    @RequestMapping(value = { "" })
    public ModelAndView attemptList(Integer pageNum, long instanceId) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<Attempt> attempts = this.attemptService.list(instanceId, pageNum, paginator);

        TaskInstance instance = this.instanceService.get(instanceId);

        Task task = this.taskService.get(instance.getTaskId());

        map.put("task", task);
        map.put("instance", instance);
        map.put("list", attempts);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

}

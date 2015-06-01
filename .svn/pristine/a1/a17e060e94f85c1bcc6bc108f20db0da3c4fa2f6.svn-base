package com.yeahmobi.yscheduler.web.controller.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.executor.TaskInstanceExecutor;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.TaskType;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.TaskVO;

/**
 * @author wukezhu
 */
@Controller
@RequestMapping(value = { TaskListController.SCREEN_NAME })
public class TaskListController extends AbstractController {

    public static final String   SCREEN_NAME = "task";

    private static final Logger  LOGGER      = LoggerFactory.getLogger(TaskListController.class);

    @Autowired
    private TaskService          taskService;

    @Autowired
    private TaskInstanceService  taskInstanceService;

    @Autowired
    private UserService          userService;

    @Autowired
    private TaskInstanceExecutor instanceExecutor;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer taskType, Integer taskStatus, String name, Long owner, Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Query query = buildQuery(map, taskType, taskStatus, name, owner);

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 1)) ? 1 : pageNum;

        List<Task> tasks = this.taskService.list(query, pageNum, paginator, UserContextHolder.getUserContext().getId());

        List<TaskVO> list = new ArrayList<TaskVO>(tasks.size());
        if (tasks != null) {
            for (Task task : tasks) {
                TaskVO vo = new TaskVO(task);
                User user = this.userService.get(task.getOwner());
                vo.setUser(user);
                list.add(vo);
            }
        }

        map.put("list", list);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

    private Query buildQuery(Map<String, Object> map, Integer taskType, Integer taskStatus, String name, Long owner) {
        Query query = new Query();

        if (taskType != null) {
            TaskType type = TaskType.valueOf(taskType);
            query.setTaskType(type);
        }

        if (taskStatus != null) {
            TaskStatus status = TaskStatus.valueOf(taskStatus);
            query.setTaskStatus(status);
        }

        query.setName(name);
        query.setOwner(owner);

        map.put("query", query);
        map.put("types", TaskType.values());
        User curUser = this.userService.get(UserContextHolder.getUserContext().getId());
        map.put("teamMembers", this.userService.listByTeam(curUser.getTeamId()));

        return query;
    }

    @RequestMapping(value = { "" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object action(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        long taskId = Long.parseLong(request.getParameter("id"));
        Task task = this.taskService.get(taskId);
        String action = request.getParameter("action");
        String actionChinese = request.getParameter("actionChinese");
        String notice = actionChinese + "作业" + task.getName();
        boolean success = false;
        try {
            if ("suspend".equals(action)) {
                task.setStatus(TaskStatus.PAUSED);
                this.taskService.update(task);
            } else if ("resume".equals(action)) {
                task.setStatus(TaskStatus.OPEN);
                this.taskService.update(task);
            } else if ("trigger".equals(action)) {

            } else if ("delete".equals(action)) {
                task.setStatus(TaskStatus.REMOVED);
                this.taskService.update(task);
            }
            success = true;
            notice += "成功！";
        } catch (Exception e) {
            success = false;
            notice += "失败！";
            notice += e.getMessage();
        }

        Paginator paginator = new Paginator();
        List<Task> list = this.taskService.list(new Query(), 0, paginator, UserContextHolder.getUserContext().getId());

        map.put("list", list);
        map.put("notice", notice);
        map.put("success", success);
        map.put("paginator", paginator);
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/trigger", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object trigger(HttpSession session, long taskId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TaskInstance instance = new TaskInstance();
            instance.setStartTime(new Date());
            instance.setStatus(TaskInstanceStatus.READY);
            instance.setTaskId(taskId);
            // 创建instance，保存instance到数据库
            this.taskInstanceService.save(instance);
            this.instanceExecutor.submit(instance);

            map.put("success", true);
        } catch (IllegalArgumentException e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        } catch (Exception e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return JSON.toJSONString(map);

    }

}

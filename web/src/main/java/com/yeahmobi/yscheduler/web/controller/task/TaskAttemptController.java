package com.yeahmobi.yscheduler.web.controller.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.common.fileserver.FileServer;
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

    private static final Logger LOGGER      = LoggerFactory.getLogger(TaskAttemptController.class);

    @Autowired
    private TaskService         taskService;

    @Autowired
    private TaskInstanceService instanceService;

    @Autowired
    private AttemptService      attemptService;

    @Autowired
    private FileServer          fileServer;

    @RequestMapping(value = { "" })
    public ModelAndView attemptList(Integer pageNum, long instanceId) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<Attempt> attempts = this.attemptService.list(instanceId, pageNum, paginator);

        TaskInstance instance = this.instanceService.get(instanceId);

        Task task = this.taskService.get(instance.getTaskId());

        map.put("task", task);
        map.put("list", attempts);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = "/getLog", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getAttemptLog(HttpSession session, long attemptId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Attempt attempt = this.attemptService.get(attemptId);
            if (this.fileServer.exists(Constants.FILESERVER_NAMESPACE_ATTEMPTLOG, attempt.getOutput())) {
                map.put("log",
                        "First 4k content only: \n"
                                + new String(this.fileServer.getContent(Constants.FILESERVER_NAMESPACE_ATTEMPTLOG,
                                                                        attempt.getOutput(), 4096),
                                             Constants.LOG_FILE_ENCODE));
                map.put("logLink",
                        this.fileServer.getDownloadLink(Constants.FILESERVER_NAMESPACE_ATTEMPTLOG, attempt.getOutput()));
            } else {
                map.put("log", attempt.getOutput());
            }

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

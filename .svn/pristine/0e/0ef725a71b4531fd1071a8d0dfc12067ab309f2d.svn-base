package com.yeahmobi.yscheduler.web.controller.workflow.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.web.controller.AbstractController;

/**
 * @author Ryan Sun
 */
@Controller
@RequestMapping(value = { CommonWorkflowCreateController.SCREEN_NAME })
public class CommonWorkflowCreateController extends AbstractController {

    public static final String   SCREEN_NAME = "common/create";

    private static final Logger  LOGGER      = LoggerFactory.getLogger(CommonWorkflowCreateController.class);

    @Autowired
    private CommonWorkflowHelper createHelper;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView createView() {
        Map<String, Object> map = new HashMap<String, Object>();
        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = { "" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object create(HttpServletRequest request, String name, Integer timeout, String description, String crontab,
                         boolean running, boolean canSkip, boolean concurrent, String dependingStatus)
                                                                                                      throws ServletException,
                                                                                                      IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.createHelper.validate(name, timeout, crontab);
            this.createHelper.createWorkflow(name, timeout, description, crontab, running);
            map.put("success", true);
        } catch (IllegalArgumentException e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("errorMsg", e.getMessage());
            map.put("success", false);
        }
        return JSON.toJSONString(map);
    }
}

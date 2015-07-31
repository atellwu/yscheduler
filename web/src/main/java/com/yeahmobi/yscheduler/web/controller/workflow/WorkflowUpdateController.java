package com.yeahmobi.yscheduler.web.controller.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

/**
 * @author Ryan Sun
 */
@Controller
@RequestMapping(value = { WorkflowUpdateController.SCREEN_NAME })
public class WorkflowUpdateController extends AbstractController {

    public static final String  SCREEN_NAME = "workflow/edit";

    private static final Logger LOGGER      = LoggerFactory.getLogger(WorkflowUpdateController.class);

    @Autowired
    private WorkflowHelper      updateHelper;

    @RequestMapping(value = { "" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object update(HttpServletRequest request, Long id, Integer timeout, String description, String crontab,
                         boolean canSkip, boolean concurrent, String condition) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.updateHelper.validate(timeout, crontab);
            List<WorkflowDetailVO> detailVos = this.updateHelper.parse(request);
            Workflow workflow = this.updateHelper.updateWorkflow(id, timeout, description, crontab, canSkip,
                                                                 concurrent, condition);
            this.updateHelper.saveWorkflowDetail(detailVos, workflow);
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

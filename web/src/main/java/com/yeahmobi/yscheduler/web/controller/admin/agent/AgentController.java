package com.yeahmobi.yscheduler.web.controller.admin.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.monitor.ActiveAgentManager;
import com.yeahmobi.yscheduler.web.controller.AbstractController;

/**
 * @author Leo Liang, Ryan Sun
 */
@Controller
@RequestMapping(value = { AgentController.SCREEN_NAME })
public class AgentController extends AbstractController {

    public static final String  SCREEN_NAME = "agent";

    private static final Logger LOGGER      = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService        agentService;

    @Autowired
    private UserService         userService;

    @Autowired
    private TeamService         teamService;

    @Autowired
    private ActiveAgentManager  activeAgentManager;

    private String              uploadPath;

    @Value("#{confProperties['storageServerUri']}")
    private String              storageServerUri;

    @PostConstruct
    public void init() {
        this.uploadPath = this.storageServerUri + "/upload";
    }

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<Agent> agents = new ArrayList<Agent>();
        if (UserContextHolder.getUserContext().isAdmin()) {
            agents = this.agentService.list(pageNum, paginator);
        } else {
            Long teamId = this.userService.get(UserContextHolder.getUserContext().getId()).getTeamId();
            agents = this.agentService.list(teamId, pageNum, paginator);
        }
        List<Team> teams = this.teamService.list();

        for (Agent agent : agents) {
            agent.setAlive(this.activeAgentManager.isActive(agent.getId()));
        }

        map.put("teams", teams);
        map.put("list", agents);
        map.put("paginator", paginator);
        map.put("uploadPath", this.uploadPath);

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object create(String name, String ip, Long teamId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.notEmpty(name, "名称不能为空");
            Validate.notEmpty(ip, "ip不能为空");

            Agent agent = new Agent();
            agent.setAlive(true);
            agent.setEnable(true);
            agent.setIp(ip);
            agent.setName(name);
            if ((teamId == null) || !UserContextHolder.getUserContext().isAdmin()) {
                long userId = UserContextHolder.getUserContext().getId();
                teamId = this.userService.get(userId).getTeamId();
            }
            agent.setTeamId(teamId);

            this.agentService.add(agent);

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

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object remove(Long agentId, String agentName) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.notNull(agentId, "Agent id不合法");
            Validate.notEmpty(agentName, "Agent name不能为空");

            this.agentService.remove(agentId, agentName);

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

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object update(Long agentId, String ip, Long teamId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.notNull(agentId, "Agent id不合法");
            Validate.notEmpty(ip, "ip不能为空");
            if ((teamId == null) || !UserContextHolder.getUserContext().isAdmin()) {
                long userId = UserContextHolder.getUserContext().getId();
                teamId = this.userService.get(userId).getTeamId();
            }
            this.agentService.update(agentId, ip, teamId);

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

    @RequestMapping(value = "/enableOrDisable", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object enableOrDisable(long agentId, boolean enable) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(agentId > 0, "Agent id不合法");
            this.agentService.updateStatus(agentId, enable);
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

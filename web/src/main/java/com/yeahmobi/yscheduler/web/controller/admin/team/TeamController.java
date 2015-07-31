package com.yeahmobi.yscheduler.web.controller.admin.team;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
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
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;

/**
 * @author Leo Liang
 */
@Controller
@RequestMapping(value = { TeamController.SCREEN_NAME })
public class TeamController extends AbstractController {

    public static final String  SCREEN_NAME = "team";

    private static final Logger LOGGER      = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamService         teamService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<Team> list = this.teamService.list(pageNum, paginator);

        map.put("list", list);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object create(String teamname) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.notEmpty(teamname, "团队名称不能为空");

            Team team = new Team();
            team.setName(teamname);

            this.teamService.add(team);

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
    public Object remove(long teamId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(teamId > 0, "团队id不合法");

            this.teamService.remove(teamId);

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

    @RequestMapping(value = "/rename", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object rename(long teamId, String teamName) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(teamId > 0, "团队id不合法");
            Validate.notEmpty(teamName, "团队名称不能为空");

            this.teamService.updateName(teamId, teamName);

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

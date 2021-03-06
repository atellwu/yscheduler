package com.yeahmobi.yscheduler.web.controller.admin.user;

import java.io.IOException;
import java.util.ArrayList;
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
import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.common.PasswordEncoder;
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.UserVO;

/**
 * @author Ryan Sun
 */
@Controller
@RequestMapping(value = { UserController.SCREEN_NAME })
public class UserController extends AbstractController {

    public static final String  SCREEN_NAME = "user";

    private static final Logger LOGGER      = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService         userService;

    @Autowired
    private TeamService         teamService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Long teamId, Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<User> list = teamId == null ? this.userService.list(pageNum, paginator) : this.userService.listByTeam(teamId,
                                                                                                                   pageNum,
                                                                                                                   paginator);
        List<Team> teams = this.teamService.list();

        Map<Long, String> teamIdNameMapping = new HashMap<Long, String>();
        for (Team team : teams) {
            teamIdNameMapping.put(team.getId(), team.getName());
        }

        List<UserVO> uservos = new ArrayList<UserVO>();

        for (User user : list) {
            UserVO uservo = new UserVO(user);
            uservo.setTeamName(user.getTeamId() == null ? null : teamIdNameMapping.get(user.getTeamId()));
            uservos.add(uservo);
        }

        map.put("list", uservos);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object create(String username, String telephone) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.notEmpty(username, "用户名不能为空");
            Validate.isTrue(Constants.USER_NAME_PATTERN.matcher(username).matches(), "用户名必须是字母，下划线或点组成，以字母开头，长度3-30个字符");
            Validate.notEmpty(telephone, "手机号码不能为空");
            Validate.isTrue(Constants.USER_TELEPHONE_PATTERN.matcher(telephone).matches(), "必须是正确的手机号码");

            User user = new User();
            user.setName(username);
            user.setEmail(username + Constants.USER_EMAIL_suffix);
            user.setPassword(PasswordEncoder.encode(username));
            user.setTelephone(telephone);

            this.userService.add(user);

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
    public Object remove(long userId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(userId > 0, "用户id不合法");

            this.userService.remove(userId);

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

    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object reset(long userId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(userId > 0, "用户id不合法");

            this.userService.resetPassword(userId);

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

    @RequestMapping(value = "/regenToken", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object regenToken(long userId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue(userId > 0, "用户id不合法");

            this.userService.regenToken(userId);

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

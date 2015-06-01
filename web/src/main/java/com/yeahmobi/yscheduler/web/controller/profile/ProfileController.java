package com.yeahmobi.yscheduler.web.controller.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.UserVO;

/**
 * @author Leo Liang
 */
@Controller
@RequestMapping(value = { ProfileController.SCREEN_NAME })
public class ProfileController extends AbstractController {

    public static final String SCREEN_NAME = "profile";

    @Autowired
    private UserService        userService;

    @Autowired
    private TeamService        teamService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Boolean teamIdIsNull) {
        Map<String, Object> map = new HashMap<String, Object>();

        User user = this.userService.get(UserContextHolder.getUserContext().getId());
        UserVO uservo = new UserVO(user);

        List<Team> teams = this.teamService.list();

        Map<Long, String> teamIdNameMapping = new HashMap<Long, String>();
        for (Team team : teams) {
            teamIdNameMapping.put(team.getId(), team.getName());
        }

        uservo.setTeamName(user.getTeamId() == null ? null : teamIdNameMapping.get(user.getTeamId()));

        map.put("user", uservo);
        map.put("teams", teams);

        if ((teamIdIsNull != null) && teamIdIsNull) {
            map.put("notice", "还没有选择所属团队，请选择!");
        }

        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = { "update" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object update(long id, String telephone, long teamId) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            Validate.isTrue(id >= 0, "id不合法");
            Validate.isTrue(teamId >= 0, "必须选择一个团队");
            Validate.notEmpty(telephone, "手机号码不能为空");
            Validate.isTrue(Constants.USER_TELEPHONE_PATTERN.matcher(telephone).matches(), "必须是正确的手机号码");

            User user = new User();
            user.setId(id);
            user.setTeamId(teamId);
            user.setTelephone(telephone);
            this.userService.update(user);

            map.put("notice", "修改成功");
            map.put("success", true);
        } catch (Exception e) {
            map.put("notice", e.getMessage());
            map.put("success", false);
        }
        return JSON.toJSONString(map);
    }
}

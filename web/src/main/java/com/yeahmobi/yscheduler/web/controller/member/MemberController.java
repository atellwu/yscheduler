package com.yeahmobi.yscheduler.web.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.common.Paginator;
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
@RequestMapping(value = { MemberController.SCREEN_NAME })
public class MemberController extends AbstractController {

    public static final String SCREEN_NAME = "member";

    @Autowired
    private UserService        userService;

    @Autowired
    private TeamService        teamService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        long userId = UserContextHolder.getUserContext().getId();
        long teamId = this.userService.get(userId).getTeamId();
        List<User> list = this.userService.listByTeam(teamId, pageNum, paginator);

        List<UserVO> uservos = new ArrayList<UserVO>();

        for (User user : list) {
            UserVO uservo = new UserVO(user);
            uservo.setTeamName(this.teamService.get(teamId).getName());
            uservos.add(uservo);
        }

        map.put("list", uservos);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }
}

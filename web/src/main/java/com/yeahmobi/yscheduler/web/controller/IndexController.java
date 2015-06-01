package com.yeahmobi.yscheduler.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wukezhu
 */
@Controller
@RequestMapping(value = { "/" })
public class IndexController extends AbstractController {

    public static final String SCREEN_NAME = "index";

    /**
     * 首页，使用说明
     */
    @RequestMapping(value = { "" })
    public ModelAndView index() {
        Map<String, Object> map = new HashMap<String, Object>();

        return screen(map, SCREEN_NAME);
    }

}

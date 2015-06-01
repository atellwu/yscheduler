package com.yeahmobi.yscheduler.web.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wukezhu
 */
public class AbstractController {

    protected ModelAndView screen(Map<String, Object> map, String pathStr) {
        // "screens/" + path1 + "/" + path2 + ... + "/main"
        String[] paths = StringUtils.split(pathStr, '/');

        StringBuilder page = new StringBuilder("screens/");

        if (paths != null) {
            for (int i = 1; i <= paths.length; i++) {
                String p = paths[i - 1];
                map.put("path" + i, p);
                page.append(p + "/");
            }
        }
        page.append("main");

        return new ModelAndView(page.toString(), map);
    }

}

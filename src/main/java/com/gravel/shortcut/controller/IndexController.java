package com.gravel.shortcut.controller;

import com.gravel.shortcut.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName IndexController
 * @Description: 页面控制器
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Controller
public class IndexController {

    @Autowired
    private ServerConfig serverConfig;

    @GetMapping(value = "/")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("url", serverConfig.getUrl());
        return mv;
    }
}

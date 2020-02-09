package com.gravel.shortcut.controller;

import com.gravel.shortcut.configuration.ServerInitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @ClassName IndexController
 * @Description: 页面控制器
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Controller
public class IndexController {

    @Resource
    private ServerInitConfiguration serverInitConfiguration;

    @GetMapping(value = "/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("domain", serverInitConfiguration.getUrl());
        return mv;
    }
}

package com.gravel.shortcut.controller;

import com.gravel.shortcut.service.UrlConvertService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName RedirectController
 * @Description: 重定向控制器
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/

@Controller
public class RedirectController {

    @Autowired
    private UrlConvertService urlConvertService;

    /**
     * 302 重定向到新的地址
     * @param request
     * @param response
     * @return
     */
    @GetMapping("*")
    public RedirectView test(HttpServletRequest request, HttpServletResponse response) {
        String shortcut = request.getServletPath().substring(1);
        String url = urlConvertService.revertUrl(shortcut).getData();
        return new RedirectView(url);
    }

}

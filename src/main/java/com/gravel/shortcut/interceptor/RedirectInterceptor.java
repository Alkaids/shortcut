package com.gravel.shortcut.interceptor;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName RedirectInterceptor
 * @Description: TODO
 * @Author gravel
 * @Date 2020/2/2
 * @Version V1.0
 **/
@Component
public class RedirectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求进入这个拦截器
        String shortCut = request.getServletPath();
        if(Strings.isNullOrEmpty(shortCut)||shortCut.contains("favicon.ico")){
            return false;
        }
        response.sendRedirect(shortCut);
        return true;
    }
}

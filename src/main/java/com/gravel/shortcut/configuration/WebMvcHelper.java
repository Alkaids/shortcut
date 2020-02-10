package com.gravel.shortcut.configuration;

import com.gravel.shortcut.configuration.exception.ShortCutException;
import com.gravel.shortcut.domain.Result;
import com.gravel.shortcut.domain.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName WebConfig
 * @Description: 资源配置
 * @Author gravel
 * @Date 2020/2/2
 * @Version V1.0
 **/
@Slf4j
@Configuration
public class WebMvcHelper implements WebMvcConfigurer {

    /**
     * 统一异常处理
     *
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            // 默认String
            Result<String> result = new Result<>();
            if (e instanceof ShortCutException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                log.info(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
            } else if (e instanceof ServletException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误！");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());

                } else {
                    message = e.getMessage();
                }
                result.setMessage(e.getMessage());
                log.error(message, e);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    // ------------------- private 方法开始

    private void responseResult(HttpServletResponse response, Result<String> result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(result.toJsonStr());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    // ------------------ private 方法结束
}

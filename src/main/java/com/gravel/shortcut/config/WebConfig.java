package com.gravel.shortcut.config;

import com.gravel.shortcut.interceptor.RedirectInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @ClassName WebConfig
 * @Description: TODO
 * @Author gravel
 * @Date 2020/2/2
 * @Version V1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private RedirectInterceptor redirectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(redirectInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/convert", "/revert","/error","/static/**");
    }
}

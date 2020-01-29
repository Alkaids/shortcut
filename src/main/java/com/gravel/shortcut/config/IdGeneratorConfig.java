package com.gravel.shortcut.config;

import com.gravel.shortcut.utils.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName IdGenerator
 * @Description: 创建id生成器的的，单例bean
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Configuration
public class IdGeneratorConfig {

    @Bean("idGenerator")
    SnowFlake snowFlake(){
        return new SnowFlake(219);
    }
}

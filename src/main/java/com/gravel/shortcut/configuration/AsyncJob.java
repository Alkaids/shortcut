package com.gravel.shortcut.configuration;

import com.gravel.shortcut.configuration.bloom.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName AsyncJob
 * @Description: TODO
 * @Author gravel
 * @Date 2020/2/9
 * @Version V1.0
 **/
@EnableAsync
@Component
@Slf4j
public class AsyncJob {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private BloomFilter bloomFilter;

    /**
     * 自定义异步线程池
     *
     * @return
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("bloom-Executor-");
        // 这个任务基本没有计算逻辑，全是和redis的io操作，所以属于IO密集型任务，最大线程数设置应为 cpu核数*2
        int thread = Runtime.getRuntime().availableProcessors() * 2;
        executor.setMaxPoolSize(thread);
        executor.setCorePoolSize(thread >> 1);
        executor.setKeepAliveSeconds(5);
        // 不设置队列最大上限
        executor.setQueueCapacity(Integer.MAX_VALUE);
        return executor;
    }

    // -------------------- 以下是异步方法

    /**
     * 将短网址和短域名异步添加到布隆过滤器中，提升相应速度
     * @param shortCut
     * @param url
     */
    @Async
    public void add2RedisAndBloomFilter(String shortCut, String url) {
        log.info("正在执行异步任务，添加[shortCut]={},[url]={} 到布隆过滤器以及redis中....",shortCut,url);
        // 放到redis里面
        redisTemplate.opsForValue().set(shortCut, url);
        // 添加到布隆过滤器
        bloomFilter.addByBloomFilter(url);
        redisTemplate.opsForValue().set(url, shortCut);
    }
}

package com.gravel.shortcut.service.impl;

import com.google.common.base.Strings;
import com.gravel.shortcut.configuration.AsyncJob;
import com.gravel.shortcut.domain.Response;
import com.gravel.shortcut.service.UrlConvertService;
import com.gravel.shortcut.configuration.bloom.BloomFilter;
import com.gravel.shortcut.utils.NumericConvertUtils;
import com.gravel.shortcut.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName UrlConvertServiceImpl
 * @Description: 短地址处理service
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Slf4j
@Service
public class UrlConvertServiceImpl implements UrlConvertService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SnowFlake idGenerator;

    @Resource
    private BloomFilter bloomFilter;

    @Resource
    private AsyncJob asyncJob;
    /**
     * 得到短地址URL
     *
     * @param url
     * @return
     */
    @Override
    public Response<String> convertUrl(String url) {
        log.info("转换开始----->[url]={}",url);
        // 如果布隆过滤器能命中，则直接返回 对应的value
        if (bloomFilter.includeByBloomFilter(url)) {
            String shortcut;
            if (!Strings.isNullOrEmpty(shortcut = redisTemplate.opsForValue().get(url))) {
                return new Response<>(shortcut);
            }
        }
        // 直接生成一个新的短地址，并存入缓存
        long nextId = idGenerator.nextId();
        // 转换为62进制
        String shortCut = NumericConvertUtils.convertTo(nextId, 62);
        log.info("转换成功----->[shortCut]={}",shortCut);
        // 将短网址和短域名异步添加到布隆过滤器中，提升响应速度
        asyncJob.add2RedisAndBloomFilter(shortCut,url);
        // 存在的话直接返回
        return new Response<>(shortCut);
    }

    /**
     * 将短地址URL 转换为正常的地址
     *
     * @param shortUrl
     * @return
     */
    @Override
    public Response<String> revertUrl(String shortUrl) {
        log.info("还原开始----->[shortUrl]={}",shortUrl);
        String shortcut = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
        String url = redisTemplate.opsForValue().get(shortcut);
        log.info("还原成功----->[真实Url]={}",url);
        return new Response<>(url);
    }
}

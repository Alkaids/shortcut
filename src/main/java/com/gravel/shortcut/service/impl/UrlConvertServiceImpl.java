package com.gravel.shortcut.service.impl;

import com.gravel.shortcut.entity.Response;
import com.gravel.shortcut.service.UrlConvertService;
import com.gravel.shortcut.service.bloom.BloomFilter;
import com.gravel.shortcut.utils.NumericConvertUtils;
import com.gravel.shortcut.utils.SnowFlake;
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
@Service
public class UrlConvertServiceImpl implements UrlConvertService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SnowFlake idGenerator;

    @Resource
    private BloomFilter bloomFilter;

    /**
     * 得到短地址URL
     *
     * @param url
     * @return
     */
    @Override
    public Response<String> convertUrl(String url) {
        if(bloomFilter.includeByBloomFilter(url)){
            return new Response<>(redisTemplate.opsForValue().get(url));
        }
        // 直接生成一个新的短地址，并存入缓存
        long nextId = idGenerator.nextId();
        // 转换为62进制
        String shortCut = NumericConvertUtils.convertTo(nextId, 62);
        // 放到redis里面
        redisTemplate.opsForValue().set(shortCut, url);

        // 添加到布隆过滤器
        bloomFilter.addByBloomFilter(url);
        redisTemplate.opsForValue().set(url, shortCut);

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
        String shortcut = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
        String url = redisTemplate.opsForValue().get(shortcut);
        return new Response<>(url);
    }
}

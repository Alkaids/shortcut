package com.gravel.shortcut.service.bloom;

import com.google.common.base.Preconditions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName BloomFilter
 * @Description: 布隆过滤器
 * @Author gravel
 * @Date 2020/2/2
 * @Version V1.0
 **/
@Component
public class BloomFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private BloomFilterHelper bloomFilterHelper;

    /**
     * 根据给定的布隆过滤器添加值
     */
    public void addByBloomFilter(String value) {
        Preconditions.checkArgument(value != null, "value不能为空");
        long[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (long i : offset) {
            redisTemplate.opsForValue().setBit(bloomFilterHelper.getBfKey(), i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public boolean includeByBloomFilter(String value) {
        Preconditions.checkArgument(value != null, "value不能为空");
        long[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (long i : offset) {
            Boolean include;
            if ((include = redisTemplate.opsForValue().getBit(bloomFilterHelper.getBfKey(), i)) == null || !include) {
                return false;
            }
        }
        return true;
    }


}

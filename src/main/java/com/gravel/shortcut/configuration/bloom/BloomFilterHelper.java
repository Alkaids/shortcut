package com.gravel.shortcut.configuration.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

/**
 * @ClassName BloomFilterHelper
 * @Description: 布隆过滤器配置类，因为本项目主要是url去重，所以默认是String类型的数据
 * @Author gravel
 * @Date 2020/2/2
 * @Version V1.0
 **/
public class BloomFilterHelper {

    /**
     * hash 次数
     */
    private int numHashFunctions;

    /**
     * 布隆过滤器大小
     */
    private long bitSize;

    /**
     * 过滤器名字
     */
    private String key;

    /**
     * 过滤器构造方法
     *
     * @param bfKey
     * @param expectedInsertions
     * @param fpp
     */
    public BloomFilterHelper(String bfKey, int expectedInsertions, double fpp) {
        this.key = bfKey;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 计算此value在bit数组中的所有位置
     *
     * @param value
     * @return
     */
    long[] murmurHashOffset(String value) {
        long[] offset = new long[numHashFunctions];

        byte[] bytes = Hashing.murmur3_128().hashString(value, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        for (int i = 1; i <= numHashFunctions; i++) {
            long nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 返回过滤器的名字
     * @return
     */
    String getBfKey(){
        return this.key;
    }

    private /* static */ long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private /* static */ long upperEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }

    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}

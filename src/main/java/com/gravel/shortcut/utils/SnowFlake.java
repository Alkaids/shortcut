package com.gravel.shortcut.utils;

/**
 * @ClassName SnowFlake 推特雪花算法生成唯一ID
 * @Description: 实现参考：https://github.com/beyondfengyu/SnowFlake
 * @Author gravel
 * @Date 2020/1/28
 * @Version V1.0
 **/
public class SnowFlake {

    /**
     * 开始时间戳，这样可以减少id长度
     */
    private final static long START_STAMP = 1580630554000L;
    /**
     * 每一部分占用的位数
     */
    //序列号占用的位数
    private final static long SEQUENCE_BIT = 12;
    //机器标识占用的位数
    private final static long WORKER_BIT = 10;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_WORKER_NUM = ~(-1L << WORKER_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);


    //所在机器的标识
    private long workerId;
    //序列号
    private long sequence = 0L;
    //上一次时间戳
    private long lastStmp = -1L;

    public SnowFlake(long workerId) {
        if (workerId > MAX_WORKER_NUM || workerId < 0) {
            throw new IllegalArgumentException("workerId can't be greater than MAX_WORKER_NUM or less than 0");
        }
        this.workerId = workerId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        lastStmp = currStmp;

        return (currStmp-START_STAMP) << SEQUENCE_BIT << WORKER_BIT | workerId << SEQUENCE_BIT | sequence;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}

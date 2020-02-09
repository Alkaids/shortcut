<h1 align="center">
  <br>
  shortcut
  <h4 align="center">
shortCut 是一个短地址生成工具，基于 twitter 的雪花算法，给新增的转换请求发号，得到新的短地址。
  </h4>
  <h5 align="center">
<a href="#Environment">Environment</a>&nbsp;&nbsp;
<a href="#Quick Start">Quick Start</a>&nbsp;&nbsp;
<a href="#Features">Features</a>&nbsp;&nbsp;
<a href="#Structure">Structure</a>&nbsp;&nbsp;
<a href="#Thanks">Thanks</a>&nbsp;&nbsp;
<a href="#License">License</a>
</h5>
  <br>
</h1>

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Alkaids/shortcut/build)![GitHub](https://img.shields.io/github/license/Alkaids/shortcut)![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/alkaids/shortcut)

## Environment

* Redis 4.0
* Java 8 +
* Maven 3.0 +


## Quick Start

```
git clone https://github.com/Alkaids/shortcut.git
mvn -Dmaven.test.skip=true clean package
java -jar shortcut-0.0.1-SNAPSHOT.jar
```

访问 http://127.0.0.1:9527/ 即可看到测试页面。

## Features

- [X] 添加布隆过滤器判断 url 是否存在
- [ ] Zookeeper 动态生成机器码（目前只支持单节点）
- [ ] 分布式部署
- [X] 增加前端页面测试
- [ ] url 请求统计
- [X] 性能测试
- [ ] 令牌桶限流


## Structure

在知乎看到[这篇贴子](https://www.zhihu.com/question/29270034/answer/46446911)谈论短地址生成的方法。
主要步骤为两个：

* 实现一个不会重复的发号器
* 每个新的请求都给它一个新的号码，转换成62进制，62进制是带有阿拉伯数字，英文大小写的格式，比较适合作为短地址的 url.

#### 发号器
直接不造轮子了，用 Twitter 的雪花算法。

```
0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 
```

 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级) 一般来说这个时间能够使用69年.
 * 10位的数据机器位，可以部署在1024个节点
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * 加起来刚好64位，为一个Long型。

#### 进制转换
通过上述发号器得到的Long类型的数据，转换为62进制，比如

```
6628238651141500928
```

转换为

```
7TDp0rS917i
```

下面这个字符串就是需要的短地址。

#### 重定向

通过 ` curl -i http://127.0.0.1:9527/7TDhjcamrAI ` 应用会匹配末端的字符串，去redis里面拿到url，然后通过状态码 302 重定向即可。

#### 性能测试

使用 JMH 做性能基准测试，环境为 `CPU: 2.2 GHz Intel Core i7; Memory: 16 GB; OS: Mac OSX`。
```
 Options options = new OptionsBuilder().include(BenchmarkTest.class.getName()+".*")
                .warmupIterations(1) // 预热
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(5)// 一共测试10轮
                .measurementTime(TimeValue.seconds(5))// 每轮测试的时长
                .forks(1)// 创建几个进程来测试
                //.threads(8)// 线程数
                .build();
```
测试效果不佳，目前考虑重构 ID 生成部分的代码，62 进制结果转换有点慢。
```
Benchmark                      Mode  Cnt   Score    Error  Units
BenchmarkTest.httprequest     thrpt    5  34.053 ±  6.381  ops/s
BenchmarkTest.serviceRequest  thrpt    5  37.282 ± 16.097  ops/s
```

## Thanks

* [spring](https://spring.io/)
* [redis](https://redis.io/)
* [guava](https://github.com/google/guava)
* [snowflake](https://developer.twitter.com/en/docs/basics/twitter-ids)
* 以及知乎上[这篇帖子](https://www.zhihu.com/question/29270034/answer/46446911)的作者


## License

Code released under the [MIT License](https://github.com/twbs/bootstrap/blob/master/LICENSE).
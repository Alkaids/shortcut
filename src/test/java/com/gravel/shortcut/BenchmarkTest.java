package com.gravel.shortcut;

import com.gravel.shortcut.service.UrlConvertService;
import okhttp3.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName BenchmarkTest
 * @Description: 性能测试
 * @Author gravel
 * @Date 2020/2/9
 * @Version V1.0
 **/
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class BenchmarkTest {

    private ConfigurableApplicationContext context;

    private UrlConvertService urlConvertService;

    private OkHttpClient client;

    public static final MediaType JSON
            = MediaType.get("application/x-www-form-urlencoded");

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchmarkTest.class.getName()+".*")
                .warmupIterations(1) // 预热
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(5)// 一共测试10轮
                .measurementTime(TimeValue.seconds(5))// 每轮测试的时长
                .forks(1)// 创建几个进程来测试
                .threads(16)// 线程数
                .build();
        new Runner(options).run();
    }


    /**
     * setup初始化容器的时候只执行一次
     */
    @Setup(Level.Trial)
    public void init(){
        context = SpringApplication.run(App.class);
        client = new OkHttpClient();
        urlConvertService = context.getBean(UrlConvertService.class);

    }

    /**
     * benchmark执行多次，此注解代表触发我们所要进行基准测试的方法
     */
    @Benchmark
    public void httprequest(){
        try{
            RequestBody requestBody = new FormBody.Builder()
                    .add("url", "http://baidu.com"+ Math.random())
                    .build();
            String repo = post("http://127.0.0.1:9527/convert",requestBody);
        }catch (Exception e){

        }
    }

    @Benchmark
    public void serviceRequest(){
        urlConvertService.convertUrl("http://baidu.com"+ Math.random());
    }


    String post(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

package com.zut.wwl.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        1.准备EventLoop对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
//        2.可以主动创建promise对象
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
//        3.任意一个线程执行计算，计算完毕后想promise填充结果
        new Thread(()->{
            log.info("开始计算。。。");
            try {
//                使出现除零异常使用promise.setFailure(e);通知主线程
                int i = 1/0;
                Thread.sleep(1000);
                promise.setSuccess(80);
            } catch (Exception e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        } ).start();
//        4.接受结果
        log.debug("等待结果。。。");
        log.debug("结果是：{}",promise.get());
    }
}

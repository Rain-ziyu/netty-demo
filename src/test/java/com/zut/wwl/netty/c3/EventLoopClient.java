package com.zut.wwl.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
//        创建启动器类
        //带有Future promise 的类型都是和异步方法配套使用的，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
//                2.添加EventLoop
                .group(new NioEventLoopGroup())
//                3.选择客户端channel实现
                .channel(NioSocketChannel.class)
//                4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override//连接建立后执行initChannel
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
//                异步非阻塞，main发起了调用，真正执行connect的是nio线程
                .connect(new InetSocketAddress("localhost", 8080));
//        2.2使用addListener（回调对象）方法异步处理结束
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
//            在nio线程建立好之后，会调用operationComplete
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
              Channel channel = channelFuture.channel();
              log.debug("{}",channel);
              channel.writeAndFlush("hello,world");
            }
        });/*
        Channel localhost = channelFuture
//2.1使用sync方法等待
//                .sync()//阻塞住当前线程，直到nio线程连接建立完毕
//                如果没有sync channel会无阻塞的向下执行
                .channel();
        log.debug("{}",localhost);
//                向服务器发送数据
//                .writeAndFlush("hello,world");
        localhost.writeAndFlush("hello,world");*/

    }
}

package com.zut.wwl.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
//        创建启动器类
        new Bootstrap()
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
                .connect(new InetSocketAddress("localhost",8080))
                .sync().channel()
//                向服务器发送数据
                .writeAndFlush("hello,world");
    }
}

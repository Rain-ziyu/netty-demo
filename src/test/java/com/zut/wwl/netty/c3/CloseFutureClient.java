package com.zut.wwl.netty.c3;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;
@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                          }
                })
                .connect(new InetSocketAddress("localhost",8080));
        Channel channel = channelFuture.sync().channel();
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close();//异步操作
                    log.debug("处理channel关闭之后");
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input");
        thread.start();
//        获取CloseFuture 对象 ，1）同步处理关闭2）异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
/* 同步关闭处理
        log.debug("waiting close ....");
        closeFuture.sync();
        log.debug("close success ....");*/
   closeFuture.addListener(new ChannelFutureListener() {
       @Override
       public void operationComplete(ChannelFuture channelFuture) throws Exception {
           log.debug("调用Nio来进行关闭处理");
           group.shutdownGracefully();
       }
   });
    }
}

package com.zut.wwl.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
//        1.启动器  负责组装netty组件、启动服务器
        new ServerBootstrap()
//           2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
                .group(new NioEventLoopGroup())
//                3.选择服务器的ServerSocketChannel IO实现
                .channel(NioServerSocketChannel.class)
//                4.boos负责处理链接  worker（child）负责处理读写 ，决定了将来我们的worker（child）能执行哪些操作（handler）
                .childHandler(
//                        5.代表和客户端进行数据读写的通道 Initializer初始化  负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
//                        6.添加具体handler
                            nioSocketChannel.pipeline().addLast(new StringDecoder());//将传输过来的ByteBuf转换为字符串
                            nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){//自定义的handler
                                @Override//处理读事件
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    super.channelRead(ctx, msg);
//                                    打印上一步转换好的字符串
                                    System.out.println(msg);
                                }
                            });
                    }
//                    绑定监听端口
                }).bind(8080);
    }
}

package com.zut.wwl.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugRead;
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //使用nio来理解阻塞
//        0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
//        1.nio方式打开服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
//        2.绑定端口
        ssc.bind(new InetSocketAddress(8080));
//        3.建立一个连接集合
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
//        4.accept建立与客户端的连接生成的SocketChannel用来与客户端通信
//            log.debug("connecting....");
            SocketChannel accept = ssc.accept();//阻塞方法,线程停止运行,如果没有连接建立，sc是null
            if(accept!=null){
            log.debug("connected....{}",accept);
            accept.configureBlocking(false);//设置SocketChannel为非阻塞
                channelList.add(accept);
            }

//        5.接收客户端发送的数据
            for(SocketChannel channel: channelList){

                int read = channel.read(buffer);//阻塞方法，线程停止运行,等到客户端发送数据  非阻塞时线程会仍然继续运行，没有到数据read返回0
                if(read>0) {
                    log.debug("before read ....{}",channel);
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("after read ....{}", channel);
                }
            }
        }
    }
}

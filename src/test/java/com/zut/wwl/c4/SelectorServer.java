package com.zut.wwl.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugRead;
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
//        1.创建一个Selector对象，管理多个channel
        Selector selector = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
//        2.建立selector和channel的联系（注册）
//        SelectionKey就是事件发生时，通过它可以知道事件和哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);

        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            SocketChannel accept = ssc.accept();
            if(accept!=null){
                log.debug("connected....{}",accept);
                accept.configureBlocking(false);
                channelList.add(accept);
            }
            for(SocketChannel channel: channelList){

                int read = channel.read(buffer);
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

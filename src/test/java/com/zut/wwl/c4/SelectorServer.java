package com.zut.wwl.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;
import static com.zut.wwl.c1_c3.ByteBufferUtil.debugRead;
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
//        1.创建一个Selector对象，管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
//        2.建立selector和channel的联系（注册）
//        SelectionKey就是事件发生时，通过它可以知道事件和哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
//        指明这个key只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}",sscKey);
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
//        3.select方法,没有事件发生，线程阻塞，有了事件select恢复执行
//            select方法在有事件未处理时他不会阻塞
            selector.select();
//        4.处理事件,selectedKeys内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            log.debug("............执行了.....................");
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
//                处理key的时候要把它从selectedKeys集合中删除，否则下次处理还会有问题
//                尽管我们处理了事件，但是只是在select方法时被阻塞，即没有事件，但是当产生了一个事件时，他的selector.selectedKeys().iterator()还是会有所有产生过的事件
//                因此我们需要进行一次remove操作
                iterator.remove();
                log.debug("............执行了iterator.hasNext().....................");
                log.debug("key:{}",key);
                if(key.isAcceptable()) {
//                处理事件5.区分事件类型
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = channel.accept();
                    log.debug("{}", accept);
//                取消处理事件
//                key.cancel();
                    accept.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(8);//attachment 附件
                    SelectionKey scKey = accept.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                }else if(key.isReadable()){
//                    这里使用tryCatch包裹防止客户端主动关闭，导致read方法抛出异常
                    try {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();//获取前面注册selector时的buffer
                        int read = channel.read(byteBuffer);//如果是正常断开，read的方法返回值是-1
                        if(read == -1){
                            key.cancel();
                        }else {
//                          debugRead(buffer);
                            split(byteBuffer);
                            if(byteBuffer.position()== byteBuffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(byteBuffer.limit()*2);
                                byteBuffer.flip();
                                newBuffer.put(byteBuffer);
                                key.attach(newBuffer);//关联新的attach,使下次可以取到新的buffer
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
//                        当客户端主动关闭时，从selectorKey中取消
//                        不取消的话会导致上面的 selector.select();不阻塞，就会再次循环
                        key.cancel();
                    }
                }
            }

        }
    }


    private static void split(ByteBuffer source) {
        source.flip();
        for(int i = 0;i<source.limit();i++){
            if(source.get(i)=='\n'){
                int length = i+1-source.position();
//                存入一个新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0;j<length;j++){
                    target.put(source.get());
                }
                debugAll(source);
            }
        }
        source.compact();
    }
}

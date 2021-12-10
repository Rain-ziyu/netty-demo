package com.zut.wwl.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.security.spec.EncodedKeySpec;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while (true){
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector,0,null);
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0;i<30000000;i++){
                        sb.append("a");
                    }
                    ByteBuffer encode = Charset.defaultCharset().encode(sb.toString());
                    int write = sc.write(encode);
                    System.out.println("第一次写入了"+write);
//                    3.判断是否有剩余内容
                    if(encode.hasRemaining()){
//                        4.关注可写事件
                        sckey.interestOps(sckey.interestOps()+SelectionKey.OP_WRITE);
                        sckey.attach(encode);
                    }
                }
                else if(key.isWritable()){
                    ByteBuffer attachment = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(attachment);
                    System.out.println(write);
//                    6.对挂载key上的buffer进行清理
                    if(!attachment.hasRemaining()){
                        key.attach(null);//清理buffer
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);//不需要再关注事件
                    }
                }
            }
        }
    }
}

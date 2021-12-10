package com.zut.wwl.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;
@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
//        创建固定数量的worker
        Worker worker = new Worker("work-0");

        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected register...{}",sc.getRemoteAddress());
//                    2.关联 selector
                    log.debug("after register...{}",sc.getRemoteAddress());
                    worker.register();
                    sc.register(worker.selector,SelectionKey.OP_READ,null);
                    log.debug("after register...{}",sc.getRemoteAddress());
                }

            }
        }
    }

   static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;

       public Worker(String name) {
           this.name = name;
       }

       public String getName() {
            return name;
        }
        private volatile  boolean start = false;
//初始化线程和selector
        public void register() throws IOException {
            if(!start) {
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
        }

        @Override
        public void run() {
            while (true){
                try {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isReadable()){
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        SocketChannel channel = (SocketChannel) key.channel();
                        log.debug("read...{}",channel.getRemoteAddress());
                        channel.read(buffer);
                        buffer.flip();
                        debugAll(buffer);
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

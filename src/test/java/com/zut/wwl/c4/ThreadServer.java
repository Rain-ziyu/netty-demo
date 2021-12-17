package com.zut.wwl.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

@Slf4j
public class ThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
//        创建固定数量的worker并初始化
//        动态获取线程数 Runtime.getRuntime().availableProcessors() 但是如果是在docker容器下，因为容器不是物理隔离的，会拿到物理cpu个数
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0;i<workers.length;i++){
            workers[i] = new Worker("worker-"+i);
        }
        AtomicInteger index = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected register...{}", sc.getRemoteAddress());
//                    2.关联 selector
                    log.debug("before register...{}", sc.getRemoteAddress());
//                    round robin 轮询
                    workers[index.getAndIncrement()%workers.length].register(sc);
                    log.debug("after register...{}", sc.getRemoteAddress());
                }

            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private volatile boolean start = false;

        //初始化线程和selector
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
//            向队列添加任务，但这个任务没有立即执行
//            queue.add(() -> {
//                try {
//                    sc.register(selector, SelectionKey.OP_READ, null);
//                } catch (ClosedChannelException e) {
//                    e.printStackTrace();
//                }
//            });


//            该方法比较特殊，就算selector.select();还没到达，即还未被阻塞，该wakeup仍然会生效
//            相当于是这个方法会提前发一张票，在下次被阻塞时，不进行阻塞
//            所以不使用队列而是将注册方法放在下面也是可以的
            selector.wakeup();//唤醒selector
            sc.register(selector, SelectionKey.OP_READ, null);
        }

        @Override
        public void run() {
            while (true) {
                try {
//                    这里开始的代码是运行在work0线程内
                    selector.select();//这里会阻塞线程，导致上面的 sc.register(worker.selector,SelectionKey.OP_READ,null);无法被执行到
//                    Runnable task = queue.poll();
//                    if (task != null) {
//                        task.run();//执行了 sc.register(selector,SelectionKey.OP_READ,null);
//                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read...{}", channel.getRemoteAddress());
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

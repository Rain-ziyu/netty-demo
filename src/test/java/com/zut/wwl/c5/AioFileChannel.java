package com.zut.wwl.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;
@Slf4j
//异步IO
public class AioFileChannel {
    public static void main(String[] args) throws IOException {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
//            参数1：ByteBuffer
//            参数2：读取的起始位置
//            参数3：附件
//            参数4：回调对象
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin....");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override//read成功
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("read completed....");
                    attachment.flip();
                    debugAll(attachment);
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });
            log.debug("read finish...");
        } catch (IOException e) {
            e.printStackTrace();
        };
        System.in.read();
    }

}

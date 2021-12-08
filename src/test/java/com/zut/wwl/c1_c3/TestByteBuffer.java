package com.zut.wwl.c1_c3;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
//        FileChannel
//        1.输入输出 2.RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
//            准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (true) {
//            从channel读取数据，向buffer写入
                int read = channel.read(byteBuffer);
                log.debug("读取到的字节：{}", read);
                if(read==-1){
                    break;
                }
//            打印buffer的内容
                byteBuffer.flip();//切换bytebuffer至读模式
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    log.debug("实际字节：{}",(char) b);
                }
//                切换为写模式
                byteBuffer.clear();//还有compact方法，是把未读完的部分向前压缩，然后切换至写模式
            }
        } catch (IOException e) {
        }

    }
}

package com.zut.wwl.c1_c3;
import java.nio.ByteBuffer;
import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});
        buffer.flip();
//        从头开始读
        buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
//        System.out.println(buffer.get());
//        mark & reset
//        mark做一个标记，记录position的位置
//        reset将position重置到mark的位置
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.mark();//加标识到索引为2的位置
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
//      get(i) 不会改变指针而是根据索引去找
        System.out.println((char) buffer.get(3));
    }
}

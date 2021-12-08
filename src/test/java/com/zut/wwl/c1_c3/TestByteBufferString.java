package com.zut.wwl.c1_c3;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

public class TestByteBufferString {
    public static void main(String[] args) {
//        将字符串转化为ByteBuffer
        String text = "hello";
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(text.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        debugAll(buffer);
//        charset
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer1);
//        wrap
        ByteBuffer buffer2 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer2);
//        转换为字符串
        String s = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(s);
        String s1 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(s1);
    }
}

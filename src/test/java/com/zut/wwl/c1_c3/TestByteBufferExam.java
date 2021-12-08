package com.zut.wwl.c1_c3;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
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

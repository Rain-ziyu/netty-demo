package com.zut.wwl.c1_c3;
import java.nio.ByteBuffer;
import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0x61);
        debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63});
        debugAll(buffer);
        buffer.get();
        debugAll(buffer);
        System.out.println(buffer.get());
        debugAll(buffer);
        buffer.flip();
        debugAll(buffer);
        System.out.println(buffer.get());
        debugAll(buffer);
        buffer.compact();
        debugAll(buffer);
        buffer.put(new byte[]{0x64,0x65});
        debugAll(buffer);
    }
}

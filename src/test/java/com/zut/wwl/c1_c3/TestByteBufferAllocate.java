package com.zut.wwl.c1_c3;
import java.nio.ByteBuffer;

public class TestByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16));
        System.out.println(ByteBuffer.allocateDirect(16));
        /*
        * class java.nio.HeapByteBuffer -java 堆内存、读写效率低、收到GC的影响
        * java.nio.DirectByteBuffer -直接内存，读写效率较高（少一次推送）不会受GC影响，分配比较低下，使用不当会造成内存泄露
        */
    }
}

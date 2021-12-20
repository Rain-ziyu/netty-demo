package com.zut.wwl.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import  com.zut.wwl.netty.Util.Log;

public class TestByteBuf {
    public static void main(String[] args) {
//        buf是会自动扩容的
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buf.getClass());
        Log.log(buf);
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<300;i++){
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
        Log.log(buf);
    }
}

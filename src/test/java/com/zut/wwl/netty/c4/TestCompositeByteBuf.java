package com.zut.wwl.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static com.zut.wwl.netty.Util.Log.log;

public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1,2,3,4});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{6,7,8,9});

/*        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(buf1).writeBytes(buf2);
        log(buffer);*/

        CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        buffer.addComponents(true,buf1,buf2);
//        注意也要进行引用计数加一操作
        log(buffer);

    }
}

package com.zut.wwl.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.zut.wwl.netty.Util.Log.log;

public class TestSlice {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});
        log(byteBuf);
//在切片过程中没有发生复制
        ByteBuf byteBuf1 = byteBuf.slice(0, 5);
//        引用计数加一
        byteBuf1.retain();
        ByteBuf byteBuf2 = byteBuf.slice(5, 5);
        log(byteBuf1);
        log(byteBuf2);
//        无法对切片后的ByteBuf进行增加值操作
        byteBuf1.writeByte('x');
        System.out.println("-----------------");
        byteBuf1.setByte(0,'b');
//        此处可证明slice方法是没有产生数据复制的
        log(byteBuf);
//        释放原有byteBuf内存,释放之后原有byteBuf1无法使用需要对上边的byteBuf1进行retain
        byteBuf.release();
        log(byteBuf1);
    }
}

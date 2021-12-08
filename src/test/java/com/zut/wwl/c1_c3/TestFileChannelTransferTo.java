package com.zut.wwl.c1_c3;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.zut.wwl.c1_c3.ByteBufferUtil.debugAll;

public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("dataTo.txt").getChannel()
        ) {
            //这样的拷贝比普通的拷贝要更快，因为底层有优化
            //这种方式是有缺陷的，一次只能传输2g数据
            long size = from.size();
            //left变量代表还剩余多少字节,分多次传输大于2g的文件
            for(long left = size; left>0;){
                System.out.println("position:"+(size-left)+"left:"+left);
//                transferTo并不会在结束时将已get过的数据清空，仍保留在channel中所以起始位置为size-left
                left -= from.transferTo(size-left,2,to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

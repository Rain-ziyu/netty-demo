package com.zut.wwl.c1_c3;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileCopy {
    public static void main(String[] args) throws IOException {
        String source = ".\\data.txt";
        String target = ".\\test";
        //ctrl+o 继承父类方法  CTRL+alt+t呼出快捷代码块
        Files.walk(Paths.get(source)).forEach(path->{
            try {
                System.out.println( path.toString());
                String targetName = path.toString().replace(source,target);
                System.out.println( targetName);
                //是目录
                if(Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }
                //是文件
                else if(Files.isRegularFile(path)){
                    Files.copy(path,Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

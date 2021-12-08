package com.zut.wwl.c1_c3;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFileWalkFileTree {
    public static void main(String[] args) throws IOException {
        String source = ".\\";
    }

    private static void dirClean() throws IOException {
        //        Files.delete(Paths.get("D:\\Pictures\\收集图片 - 副本"));
        Files.walkFileTree(Paths.get("D:\\Pictures\\收集图片 - 副本"),new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>进入"+dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("<====退出"+dir);
//                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    //遍历筛选后缀为jpg的文件
    private static void m2() throws IOException {
        AtomicInteger jpgCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("D:\\Pictures\\"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".jpg")){
                    System.out.println(""+file);
                    jpgCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("满足条件的文件个数"+jpgCount);
    }
//遍历所有指定文件夹下的所有目录
    private static void m1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("D:\\Pictures\\"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>"+dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(""+file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("目录个数"+dirCount);
        System.out.println("文件个数"+fileCount);
    }
}

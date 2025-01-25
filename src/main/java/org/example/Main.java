package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * m3u8转MP4
 */
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        //String path = "D:/tx_download";  //文件路径
        String path;  //文件路径
        String ffmpegPath;
        if (args.length == 0) {
            path = "./download";
            ffmpegPath = "D:/ffmpeg";
        } else if (args.length == 2) {
            path = args[0];
            ffmpegPath = args[1];
        } else {
            log.error("需要2个参数");
            return;
        }
        AtomicInteger sum = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        CountDownLatch countDownLatch = new CountDownLatch(sum.get());
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {//遍历处理任务
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.getFileName().toString().contains(".m3u8")) {
                    return super.visitFile(file, attrs);
                }
                sum.incrementAndGet();
                log.debug(file);
                //处理文件路径
                String resourcePath = file.toRealPath().toString();
                String fileName = file.getFileName().toString();
                String targetName = fileName.substring(0, fileName.lastIndexOf("."));
                String targetPath = resourcePath;
                targetPath = resourcePath.substring(0, targetPath.lastIndexOf("\\"));
                targetPath = resourcePath.substring(0, targetPath.lastIndexOf("\\")) + "\\" + targetName;

                //转码
                String cmdTemplate = ffmpegPath + "/ffmpeg.exe -allowed_extensions ALL -protocol_whitelist \"file,http,crypto,tcp\" -i \"%s\" -c copy \"%s\"";
                String cmdTmp = String.format(cmdTemplate, resourcePath, targetPath);
                cmdTmp = cmdTmp.replace("\\", "/");
                final String cmd = cmdTmp;
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
                builder.redirectErrorStream(true);
                try {
                    builder.start();
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                    log.error(e);
                }


                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                log.error("文件访问错误：{}", file.getFileName().toString());
                return super.visitFileFailed(file, exc);
            }
        });

        log.info("总任务数：{}， 成功：{}， 失败：{}", sum.get(), success.get(), fail.get());
    }
}
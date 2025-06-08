package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Value("${music.path}")
    private String musicPath;

    @Resource
    private MusicService musicService;

    @GetMapping("/list")
    public Result<List<Music>> getAllMusic() {
        return Result.success(musicService.getMusicList());
    }

    @PostMapping("/add")
    public Result<Boolean> addMusic(@RequestBody Music music) {
        return Result.success(musicService.addMusic(music));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteMusic(@PathVariable("id") Long id) {
        return Result.success(musicService.deleteMusic(id));
    }

    @GetMapping("/local/{fileName:.+}")
    public void getLocalMusic(@PathVariable String fileName,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        // 构建安全路径
        Path basePath = Paths.get(musicPath).toAbsolutePath().normalize();
        Path targetPath = basePath.resolve(fileName).normalize();

        // 检查是否在允许的目录下
        if (!targetPath.startsWith(basePath)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = targetPath.toFile();
        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        long fileLength = file.length();
        String range = request.getHeader("Range");

        // 设置基本响应头
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(fileLength));

        // 根据文件扩展名设置正确的Content-Type
        String contentType = getContentType(fileName);
        response.setContentType(contentType);

        // 设置缓存控制头
        response.setHeader("Cache-Control", "public, max-age=3600");
        response.setDateHeader("Expires", System.currentTimeMillis() + 3600000);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
             ServletOutputStream outputStream = response.getOutputStream()) {

            if (range != null && range.startsWith("bytes=")) {
                // 处理Range请求
                handleRangeRequest(range, fileLength, randomAccessFile, response, outputStream);
            } else {
                // 处理普通请求
                response.setStatus(HttpServletResponse.SC_OK);
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = randomAccessFile.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            outputStream.flush();
        }
    }

    private void handleRangeRequest(String range, long fileLength,
                                    RandomAccessFile randomAccessFile,
                                    HttpServletResponse response,
                                    ServletOutputStream outputStream) throws IOException {

        // 解析Range头 "bytes=start-end"
        String[] ranges = range.substring(6).split("-");
        long start = 0;
        long end = fileLength - 1;

        try {
            if (ranges.length >= 1 && !ranges[0].isEmpty()) {
                start = Long.parseLong(ranges[0]);
            }
            if (ranges.length >= 2 && !ranges[1].isEmpty()) {
                end = Long.parseLong(ranges[1]);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }

        // 验证范围
        if (start > end || start < 0 || end >= fileLength) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            response.setHeader("Content-Range", "bytes */" + fileLength);
            return;
        }

        long contentLength = end - start + 1;

        // 设置206部分内容响应
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        response.setHeader("Content-Length", String.valueOf(contentLength));

        // 读取指定范围的数据
        randomAccessFile.seek(start);
        byte[] buffer = new byte[8192];
        long remaining = contentLength;

        while (remaining > 0) {
            int toRead = (int) Math.min(buffer.length, remaining);
            int bytesRead = randomAccessFile.read(buffer, 0, toRead);

            if (bytesRead == -1) {
                break;
            }

            outputStream.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }
    }

    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "flac":
                return "audio/flac";
            case "aac":
                return "audio/aac";
            case "ogg":
                return "audio/ogg";
            case "m4a":
                return "audio/mp4";
            case "wma":
                return "audio/x-ms-wma";
            default:
                return "audio/mpeg"; // 默认
        }
    }
}

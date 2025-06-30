package org.song.sharedmusicplayer.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${music.path:#{null}}")
    private String musicPath;

    private final String uploadDir;

    public FileController() {
        // 如果配置文件中定义了music.path就使用它，否则使用默认路径
        this.uploadDir = Optional.ofNullable(musicPath)
                .filter(path -> !path.trim().isEmpty())
                .orElse(System.getProperty("user.dir") + "/uploads/");
    }

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成新的文件名
        String fileName = UUID.randomUUID() + suffix;
        File uploadDir = new File(this.uploadDir);
        // 如果目录不存在，则创建
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if (!mkdirs) {
                return Result.error(400, "创建目录失败");
            }
        }
        try {
            String filePath = this.uploadDir + File.separator + fileName;
            File finalFile = new File(filePath);
            file.transferTo(finalFile);
            return Result.success("文件上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(400, "文件上传失败");
        }
    }

//    @GetMapping("/download/{filename}")
    @GetMapping("/download")
    public Result<String> downloadFile(@RequestParam String filename, HttpServletResponse response) {
        String filePath = this.uploadDir + filename;
        File file = new File(filePath);
        if (!file.exists()) {
            return Result.error(400, "文件不存在");
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        try {
            Files.copy(file.toPath(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件下载失败");
        }
        return Result.success("文件下载成功");
    }
}
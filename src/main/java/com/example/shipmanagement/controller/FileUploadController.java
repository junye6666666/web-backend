package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 保证文件名唯一
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 本地存储路径，请改为你电脑存在的实际路径
        // 注意：实际生产环境通常上传到 OSS (阿里云/七牛云)
        file.transferTo(new File("D:\\upload\\" + filename));
        return Result.success("D:\\upload\\" + filename);
    }
}
package com.school.uniform.controller;

import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.service.QCloudFileManager;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/account")
@WebResponse
public class TestController {
    @Autowired
    QCloudFileManager qCloudFileManager;

    @PostMapping("/uploadFile")
    public Object uploadFile(
            @RequestParam MultipartFile file
    ) {
        // 首先获取 newName
        String newNameWithoutType = String.valueOf(ConstantUtil.generateId());
        String newNameWithType = this.qCloudFileManager.buildNewFileNameWithType(
                file, newNameWithoutType
        );
        String ansUrl = null;
        try {
            ansUrl = qCloudFileManager.uploadOneFile(
                    file,
                    newNameWithoutType
            );
        } catch (IOException e) {
            return "上传文件失败";
        }
        // 要删除文件
        ConstantUtil.deleteFileUnderProjectDir(newNameWithType);
        // 返回最终结果
        return ansUrl;
    }


}

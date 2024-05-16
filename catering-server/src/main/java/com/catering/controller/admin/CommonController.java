package com.catering.controller.admin;
import com.catering.constant.MessageConstant;
import com.catering.result.Result;
import com.catering.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api("通用接口")
@Slf4j
public class CommonController {
    @Autowired
    AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        try {
            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String index = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFile = UUID.randomUUID() + index;
            return Result.success(aliOssUtil.upload(fileBytes, newFile));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}

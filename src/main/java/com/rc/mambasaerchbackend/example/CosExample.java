package com.rc.mambasaerchbackend.example;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.rc.mambasaerchbackend.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 腾讯云 COS 对象存储使用示例
 */
@Slf4j
@Tag(name = "示例-腾讯云COS", description = "COS 对象存储操作示例接口")
@RestController
@RequestMapping("/example/cos")
public class CosExample {

    @Value("${cos.secret-id:default}")
    private String secretId;

    @Value("${cos.secret-key:default}")
    private String secretKey;

    @Value("${cos.bucket-name:default}")
    private String bucketName;

    @Value("${cos.region:ap-guangzhou}")
    private String regionName;

    private COSClient cosClient;

    @PostConstruct
    public void init() {
        if ("default".equals(secretId)) {
            log.warn("[COS] 未配置 COS 密钥，COS 示例不可用。请在 application-{env}.yml 中添加 cos 配置。");
            return;
        }
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        cosClient = new COSClient(cred, clientConfig);
        log.info("[COS] COSClient 初始化完成, bucket={}, region={}", bucketName, regionName);
    }

    @PreDestroy
    public void destroy() {
        if (cosClient != null) {
            cosClient.shutdown();
        }
    }

    @Operation(summary = "上传文件到COS")
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile file) {
        if (cosClient == null) {
            return BaseResponse.success("COS 未配置，无法上传。请检查配置文件。");
        }

        String key = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("cos_upload_", ".tmp");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, tempFile);
            PutObjectResult result = cosClient.putObject(putRequest);

            String url = "https://" + bucketName + ".cos." + regionName + ".myqcloud.com/" + key;
            log.info("[COS] 上传成功: {}", url);
            return BaseResponse.success(url);

        } catch (IOException e) {
            log.error("[COS] 上传失败", e);
            return BaseResponse.success("上传失败: " + e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Operation(summary = "删除COS文件")
    @DeleteMapping("/delete")
    public BaseResponse<Void> delete(@RequestParam String key) {
        if (cosClient == null) {
            return BaseResponse.success();
        }
        cosClient.deleteObject(bucketName, key);
        log.info("[COS] 删除成功: {}", key);
        return BaseResponse.success();
    }
}

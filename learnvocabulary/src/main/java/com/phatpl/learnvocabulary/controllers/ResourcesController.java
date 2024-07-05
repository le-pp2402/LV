package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.utils.BuildResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/resources")
public class ResourcesController {

    private final MinioClient minioClient;

    @Value("${MinIO_BUCKETNAME}")
    private String bucketName;

    @Autowired
    public ResourcesController(MinioClient minioClient) {

        this.minioClient = minioClient;
    }

    @GetMapping
    public ResponseEntity checkConnection() {
        try {
            var buckets = minioClient.listBuckets();
            return BuildResponse.ok(buckets.size());
        } catch (Exception e) {
            return BuildResponse.notFound(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam(value = "file", required = true) MultipartFile request) {
        try {
            InputStream input = request.getInputStream();
            PutObjectArgs putObj = PutObjectArgs
                    .builder()
                    .contentType(request.getContentType())
                    .stream(input, input.available(), -1)
                    .bucket(bucketName)
                    .object(request.getResource().getFilename())
                    .build();
            minioClient.putObject(putObj);
            return BuildResponse.ok("uploaded");
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}

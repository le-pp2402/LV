package com.phatpl.learnvocabulary.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FileService {
    private final MinioClient minioClient;

    @Autowired
    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadVideo(MultipartFile video, String filename) throws Exception {
        String bucketName = "videos";
        InputStream input = video.getInputStream();
        filename += String.valueOf(System.currentTimeMillis());
        PutObjectArgs putObj = PutObjectArgs
                .builder()
                .contentType(video.getContentType())
                .stream(input, input.available(), -1)
                .bucket(bucketName)
                .object(filename)
                .build();
        minioClient.putObject(putObj);
        return bucketName + "/" + filename;
    }

    public String uploadDocument(MultipartFile document, String filename) throws Exception {
        String bucketName = "documents";
        InputStream input = document.getInputStream();
        filename += String.valueOf(System.currentTimeMillis());
        PutObjectArgs putObj = PutObjectArgs
                .builder()
                .contentType(document.getContentType())
                .stream(input, input.available(), -1)
                .bucket(bucketName)
                .object(filename)
                .build();
        minioClient.putObject(putObj);
        return bucketName + "/" + filename;
    }
}

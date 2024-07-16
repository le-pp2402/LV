package com.phatpl.learnvocabulary.services;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class MinIOService {
    private final MinioClient minioClient;

    @Autowired
    public MinIOService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String newFileName(String filename) {
        log.info(filename);
        String[] words = filename.split("[ ]");
        StringBuilder str = new StringBuilder();
        for (var word : words) {
            word = word.trim();
            if (word.isEmpty()) continue;
            str.append(word).append("_");
        }
        str.deleteCharAt(str.length() - 1);
        log.info(str.toString());
        return str.toString();
    }

    public String uploadVideo(MultipartFile video, String filename) throws Exception {
        String bucket = "videos";
        InputStream input = video.getInputStream();
        filename = newFileName(filename);
        PutObjectArgs putObj = PutObjectArgs
                .builder()
                .contentType(video.getContentType())
                .stream(input, input.available(), -1)
                .bucket(bucket)
                .object(filename)
                .build();
        minioClient.putObject(putObj);
        return bucket + "/" + filename;
    }

    public String uploadDocument(MultipartFile document, String filename) throws Exception {
        String bucket = "documents";
        InputStream input = document.getInputStream();
        filename = newFileName(filename);
        PutObjectArgs putObjectArgs = PutObjectArgs
                .builder()
                .contentType(document.getContentType())
                .stream(input, input.available(), -1)
                .bucket(bucket)
                .object(filename)
                .build();
        minioClient.putObject(putObjectArgs);
        return bucket + "/" + filename;
    }

    public InputStream getFile(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucket = path.substring(0, path.indexOf("/"));
        String file = path.substring(path.indexOf("/") + 1);
        log.info("{} {}", bucket, file);
        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(bucket)
                .object(file)
                .build();
        return minioClient.getObject(getObjectArgs);
    }

    public void delete(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucket = path.substring(0, path.indexOf("/"));
        String file = path.substring(path.indexOf("/") + 1);
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(file)
                .build();
        minioClient.removeObject(removeObjectArgs);
        log.info("path = " + path + " file = " + file);
    }

}

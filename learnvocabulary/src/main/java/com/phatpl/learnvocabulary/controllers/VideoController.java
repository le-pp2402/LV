package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.services.MinIOService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    MinIOService minIOService;

    @GetMapping("/{folder}/video/{file}")
    public ResponseEntity loadVideo(@PathVariable("folder") String folder, @PathVariable("file") String file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var response = getVideo(folder, file);
        byte[] resource = response.readAllBytes();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/{folder}/thumbnail")
    public ResponseEntity loadThumbnail(@PathVariable("folder") String folder) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var response = minIOService.getImage(folder + "/thumbnail");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response.readAllBytes());
    }

    public InputStream getVideo(String name, String file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minIOService.getFile(name + "/video/" + file);
    }
}

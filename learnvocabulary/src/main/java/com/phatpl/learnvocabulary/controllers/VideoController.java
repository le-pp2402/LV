package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.services.MinIOService;
import com.phatpl.learnvocabulary.services.ResourceService;
import io.minio.errors.MinioException;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/video")
public class VideoController {

    MinIOService minIOService;
    ResourceService resourceService;

    @Autowired
    public VideoController(MinIOService minIOService, ResourceService resourceService) {
        this.minIOService = minIOService;
        this.resourceService = resourceService;
    }

    @GetMapping("/{folder}/video/{file}")
    public ResponseEntity loadVideo(@PathVariable("folder") String folder,
                                    @PathVariable("file") String file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        var response = resourceService.getVideo(folder, file);
        byte[] resource = response.readAllBytes();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/{folder}/thumbnail")
    public ResponseEntity loadThumbnail(@PathVariable("folder") String folder)
            throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        var response = minIOService.getImage(folder + "/thumbnail");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response.readAllBytes());
    }

}

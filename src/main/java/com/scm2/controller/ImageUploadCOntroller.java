package com.scm2.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.service.ImageService;

@RestController
@RequestMapping("/api/image")
public class ImageUploadCOntroller {

    private ImageService imageService;

    public ImageUploadCOntroller(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/uploadimages")
    public ResponseEntity<String> uploadImage(@RequestParam(value = "image") MultipartFile uploadFile) {

        String data = imageService.uploadImage(uploadFile);

        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);

    }

}

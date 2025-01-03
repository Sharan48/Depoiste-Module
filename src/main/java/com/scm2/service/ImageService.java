package com.scm2.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public String uploadImage(MultipartFile userImage);

    String getUrlFromPublicId(String publicId);
}

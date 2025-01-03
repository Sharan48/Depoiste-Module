package com.scm2.servicesimp;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm2.service.ImageService;

@Service
public class ImageServiceImp implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImp(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile userImage) {
        try {
            String fileName = UUID.randomUUID().toString();
            byte[] data = new byte[userImage.getInputStream().available()];
            userImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", fileName));
            return this.getUrlFromPublicId(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image upload failed!");
        }

    }

    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary.url().transformation(new Transformation<>().width(500)
                .height(500)
                .crop("fill")).generate(publicId);

    }

}

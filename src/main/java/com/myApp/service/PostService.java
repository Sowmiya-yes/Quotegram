package com.myApp.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.myApp.config.AWSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PostService {

    @Autowired
    AWSConfig awsConfig;

    public void uploadFile(MultipartFile multipartFile) throws IOException, FileNotFoundException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();

        System.out.println(multipartFile.getOriginalFilename());

        awsConfig.getAmazonS3Client().putObject(
                new PutObjectRequest(awsConfig.getBucketName(), multipartFile.getOriginalFilename(), file));

        System.out.println("Upload done");
    }
}

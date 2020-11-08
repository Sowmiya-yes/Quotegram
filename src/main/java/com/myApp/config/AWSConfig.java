package com.myApp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class AWSConfig {
    private AmazonS3Client amazonS3Client;
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @PostConstruct
    private void initializeAWSS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        amazonS3Client = new AmazonS3Client(awsCredentials);
        System.out.println("Connected with Amazon S3");
    }

    public AmazonS3Client getAmazonS3Client() {
        return amazonS3Client;
    }
}

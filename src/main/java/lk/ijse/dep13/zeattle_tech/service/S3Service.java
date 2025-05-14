package lk.ijse.dep13.zeattle_tech.service;

import lk.ijse.dep13.zeattle_tech.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    S3Client s3Client;

    public String uploadFile(InputStream inputStream, long contentLength, String fileName, String contentType){
        String key = "products_images/" + UUID.randomUUID() + "-" + fileName;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("zeattle-app-bucket")
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        return key;
    }
}

package lk.ijse.dep13.zeattle_tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {

    @Autowired
    S3Client s3Client;

    @Autowired
    S3Presigner s3Presigner;

    public String uploadFile(InputStream inputStream, long contentLength, String key, String contentType){
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("zeattle-app-bucket")
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        return key;
    }

    public void deleteFile(String key){
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket("zeattle-app-bucket").key(key).build());
    }

    public InputStream getFile(String key){
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(GetObjectRequest.builder().bucket("zeattle-app-bucket").key(key).build());
        return object;
    }

    public URL generatePresignedUrl(String key, Duration validFor){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket("zeattle-app-bucket").key(key).build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(validFor)
                .getObjectRequest(getObjectRequest).build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }
}

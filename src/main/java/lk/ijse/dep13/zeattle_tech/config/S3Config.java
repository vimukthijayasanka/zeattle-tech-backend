package lk.ijse.dep13.zeattle_tech.config;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class S3Config {

    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final String region;

    public S3Config(@Value("${aws.credential.path}") String path) throws IOException {
        if (path == null) throw new NullPointerException("path is null");

        String content = Files.readString(Path.of(path));
        JSONObject json = new JSONObject(content);
        this.accessKey = json.getString("aws_access_key_id");
        this.secretKey = json.getString("aws_secret_access_key");
        this.bucketName = json.getString("aws_bucket_name");
        this.region = json.getString("aws_region");
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}

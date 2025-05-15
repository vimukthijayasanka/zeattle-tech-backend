package lk.ijse.dep13.zeattle_tech.controller;

import lk.ijse.dep13.zeattle_tech.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@ComponentScan
@RequestMapping("/app")
public class TestController {

    @Autowired
    S3Service s3Service;

    @PostMapping(consumes = "multipart/form-data", produces = "application/text")
    public String test(MultipartFile multipartFile) throws IOException {
        return s3Service.uploadFile(multipartFile.getInputStream(),multipartFile.getSize(),
                multipartFile.getOriginalFilename(), multipartFile.getContentType());
    }
}

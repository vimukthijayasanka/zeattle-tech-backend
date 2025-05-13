package lk.ijse.dep13.zeattle_tech.service.image;

import lk.ijse.dep13.zeattle_tech.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImage(Long id);
    void deleteImageById(Long id);
    Image saveImage(MultipartFile file, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}

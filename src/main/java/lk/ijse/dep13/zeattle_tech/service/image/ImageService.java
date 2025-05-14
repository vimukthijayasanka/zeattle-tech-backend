package lk.ijse.dep13.zeattle_tech.service.image;

import lk.ijse.dep13.zeattle_tech.dto.ImageDTO;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImage(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}

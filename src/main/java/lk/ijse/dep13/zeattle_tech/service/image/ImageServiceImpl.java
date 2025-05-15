package lk.ijse.dep13.zeattle_tech.service.image;

import lk.ijse.dep13.zeattle_tech.dto.ImageDTO;
import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.ImageRepository;
import lk.ijse.dep13.zeattle_tech.service.S3Service;
import lk.ijse.dep13.zeattle_tech.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final S3Service s3Service;

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image Not Found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            imageRepository.delete(image.get());
            s3Service.deleteFile(image.get().getImageUrl());
        } else {
            throw new ResourceNotFoundException("Image Not Found with id " + id);
        }
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductEntityById(productId);
        List<ImageDTO> savedImageDTO = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
                String fileName = file.getOriginalFilename();
                String extension = getFileExtension(fileName);
                String key = "products_images/" + productId + "/" + UUID.randomUUID() + "." + extension;

                String s3Key = s3Service.uploadFile(file.getInputStream(), file.getSize(), key, file.getContentType());

                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setProduct(product);
                image.setImageUrl(s3Key);
                Image saveImage = imageRepository.save(image);

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageId(saveImage.getId());
                imageDTO.setImageName(saveImage.getFileName());
                imageDTO.setImageUrl(s3Service.generatePresignedUrl(saveImage.getImageUrl(), Duration.ofMinutes(12)).toString());
                savedImageDTO.add(imageDTO);
            } catch (IOException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDTO;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImage(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageUrl(s3Service.uploadFile(file.getInputStream(), file.getSize(),
                    file.getOriginalFilename(), file.getContentType()));
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

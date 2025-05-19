package lk.ijse.dep13.zeattle_tech.service.util;

import lk.ijse.dep13.zeattle_tech.dto.ImageDTO;
import lk.ijse.dep13.zeattle_tech.dto.OrderDTO;
import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.dto.UserDTO;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import lk.ijse.dep13.zeattle_tech.entity.Order;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.repository.ImageRepository;
import lk.ijse.dep13.zeattle_tech.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Transformer {

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOS = images.stream()
                .map(this::imageToImageDTO).toList();
        productDTO.setImages(imageDTOS);
        return productDTO;
    }

    public List<ProductDTO> ProductsToProductDTOs(List<Product> products) {
        return products.stream().map(this::productToProductDTO).toList();
    }

    public ImageDTO imageToImageDTO(Image image) {
        String preSignedUrl = s3Service.generatePresignedUrl(image.getImageUrl(), Duration.ofMinutes(15)).toString();// <-- Add this
        return new ImageDTO(
                image.getId(),
                image.getFileName(),
                preSignedUrl
        );
    }

    public OrderDTO orderToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    public UserDTO userToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

package lk.ijse.dep13.zeattle_tech.service.util;

import lk.ijse.dep13.zeattle_tech.dto.ImageDTO;
import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class Transformer {

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOS = images.stream().map(image -> modelMapper
                .map(image, ImageDTO.class)).toList();
        productDTO.setImages(imageDTOS);
        return productDTO;
    }

    public List<ProductDTO> ProductsToProductDTOs(List<Product> products) {
        return products.stream().map(this::productToProductDTO).toList();
    }
}

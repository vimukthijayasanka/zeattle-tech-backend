package lk.ijse.dep13.zeattle_tech.service.product;

import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.dto.request.AddProductRequestTO;
import lk.ijse.dep13.zeattle_tech.dto.request.ProductUpdateRequestTO;
import lk.ijse.dep13.zeattle_tech.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(AddProductRequestTO product);
    ProductDTO getProductById(Long id);
    Product getProductEntityById(Long id);
    void deleteProductById(Long id);
    ProductDTO updateProduct(ProductUpdateRequestTO request, Long productId);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> getProductsByBrand(String brand);
    List<ProductDTO> getProductsByCategoryAndBrand(String category, String brand);
    List<ProductDTO> getProductsByName(String productName);
    List<ProductDTO> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}

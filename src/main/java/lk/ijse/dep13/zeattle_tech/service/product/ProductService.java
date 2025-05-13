package lk.ijse.dep13.zeattle_tech.service.product;

import lk.ijse.dep13.zeattle_tech.dto.request.AddProductRequestTO;
import lk.ijse.dep13.zeattle_tech.dto.request.ProductUpdateRequestTO;
import lk.ijse.dep13.zeattle_tech.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequestTO product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequestTO request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String productName);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}

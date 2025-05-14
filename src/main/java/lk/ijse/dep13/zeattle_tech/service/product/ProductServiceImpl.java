package lk.ijse.dep13.zeattle_tech.service.product;

import lk.ijse.dep13.zeattle_tech.dto.request.AddProductRequestTO;
import lk.ijse.dep13.zeattle_tech.dto.request.ProductUpdateRequestTO;
import lk.ijse.dep13.zeattle_tech.entity.Category;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.exception.ProductNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.CategoryRepository;
import lk.ijse.dep13.zeattle_tech.repository.ProductRepository;
import lk.ijse.dep13.zeattle_tech.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product Not Found");});
        Optional<Product> product = productRepository.findById(id);
        for (Image image : product.get().getImages()) {
            s3Service.deleteFile(image.getImageUrl());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countProductsByBrandAndName(brand, name);
    }

    @Override
    public Product addProduct(AddProductRequestTO request) {
        /*
        Check the category is available in DB, if it's not in the
        DB save it as new category, otherwise save the new Product
         */

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequestTO request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getStock(),
                request.getDescription(),
                category);
    }

    @Override
    public Product updateProduct(ProductUpdateRequestTO request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository :: save)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequestTO request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

}

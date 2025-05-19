package lk.ijse.dep13.zeattle_tech.service.product;

import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.dto.request.AddProductRequestTO;
import lk.ijse.dep13.zeattle_tech.dto.request.ProductUpdateRequestTO;
import lk.ijse.dep13.zeattle_tech.entity.Category;
import lk.ijse.dep13.zeattle_tech.entity.Image;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.exception.AlreadyExistsException;
import lk.ijse.dep13.zeattle_tech.exception.ProductNotFoundException;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.CategoryRepository;
import lk.ijse.dep13.zeattle_tech.repository.ProductRepository;
import lk.ijse.dep13.zeattle_tech.service.S3Service;
import lk.ijse.dep13.zeattle_tech.service.util.Transformer;
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
    private final Transformer transformer;

    @Override
    public ProductDTO getProductById(Long id) {
       return transformer.productToProductDTO(productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product Not Found " + id)));
    }

    @Override
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found " + id));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found " + id));

        for (Image image : product.getImages()) {
            s3Service.deleteFile(image.getImageUrl());
        }
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return transformer.ProductsToProductDTOs(productList);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<Product> productsByCategoryName = productRepository.findByCategoryName(category);
        if(productsByCategoryName.isEmpty()){
            throw new ProductNotFoundException("There is no product with category " + category);
        }
        return transformer.ProductsToProductDTOs(productsByCategoryName);
    }

    @Override
    public List<ProductDTO> getProductsByBrand(String brand) {
        List<Product> productsByBrand = productRepository.findByBrand(brand);
        if(productsByBrand.isEmpty()){
            throw new ProductNotFoundException("There is no product with brand " + brand);
        }
        return transformer.ProductsToProductDTOs(productsByBrand);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryAndBrand(String category, String brand) {
        List<Product> byCategoryNameAndBrand = productRepository.findByCategoryNameAndBrand(category, brand);
        if(byCategoryNameAndBrand.isEmpty()){
            throw new ResourceNotFoundException("There is no product with category " + category + " and brand " + brand);
        }
        return transformer.ProductsToProductDTOs(byCategoryNameAndBrand);
    }

    @Override
    public List<ProductDTO> getProductsByName(String productName) {
        List<Product> productByName = productRepository.findByName(productName);
        if(productByName.isEmpty()){
            throw new ResourceNotFoundException("There is no product with name " + productName);
        }
        return transformer.ProductsToProductDTOs(productByName);
    }

    @Override
    public List<ProductDTO> getProductsByBrandAndName(String brand, String name) {
        List<Product> productByBrandAndName = productRepository.findByBrandAndName(brand, name);
        if(productByBrandAndName.isEmpty()){
            throw new ResourceNotFoundException("There is no product with brand " + brand + " and " + name);
        }
        return transformer.ProductsToProductDTOs(productByBrandAndName);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countProductsByBrandAndName(brand, name);
    }

    @Override
    public ProductDTO addProduct(AddProductRequestTO request) {
        /*
        Check the category is available in DB, if it's not in the
        DB save it as new category, otherwise save the new Product
         */

        if (productExists(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException(request.getName() + " " + request.getBrand() + " already exists, you may update this product instead of adding another product");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        Product product = productRepository.save(createProduct(request, category));
        return transformer.productToProductDTO(product);
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

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    @Override
    public ProductDTO updateProduct(ProductUpdateRequestTO request, Long productId) {
        Product product = productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        return transformer.productToProductDTO(product);
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequestTO request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        existingProduct.setCategory(category);
        return existingProduct;
    }

}

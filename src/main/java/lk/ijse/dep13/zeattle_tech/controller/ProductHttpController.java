package lk.ijse.dep13.zeattle_tech.controller;

import lk.ijse.dep13.zeattle_tech.dto.ProductDTO;
import lk.ijse.dep13.zeattle_tech.dto.request.AddProductRequestTO;
import lk.ijse.dep13.zeattle_tech.dto.request.ProductUpdateRequestTO;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.exception.AlreadyExistsException;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.response.ApiResponse;
import lk.ijse.dep13.zeattle_tech.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductHttpController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductDTO> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("success", allProducts));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try{
            ProductDTO product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> saveProduct(@RequestBody AddProductRequestTO product) {
        try{
            ProductDTO addProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add product successfully", addProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequestTO product, @PathVariable Long productId) {
        try{
            ProductDTO updateProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Update product successfully", updateProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try{
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product successfully", productId));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String name) {
        try {
            List<ProductDTO> productList = productService.getProductsByBrandAndName(brandName, name);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brandName) {
        try {
            List<ProductDTO> productList = productService.getProductsByCategoryAndBrand(category, brandName);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<ProductDTO> productList = productService.getProductsByName(name);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/by-brand/{brandName}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brandName) {
        try {
            List<ProductDTO> productList = productService.getProductsByBrand(brandName);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<ProductDTO> productList = productService.getProductsByCategory(category);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/count-by-brand-and-name/products")
    public ResponseEntity<ApiResponse> getCountProductsByBrandAndName(@RequestParam String brandName, @RequestParam String name) {
        try{
            Long countProductsByBrandAndName = productService.countProductsByBrandAndName(brandName, name);
            return ResponseEntity.ok(new ApiResponse("Product count is ", countProductsByBrandAndName));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

}

package lk.ijse.dep13.zeattle_tech.repository;

import lk.ijse.dep13.zeattle_tech.entity.Category;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Long id(Long id);

    List<Product> findByCategoryName(String categoryName);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByName(String name);
    List<Product> findByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    boolean existsByNameAndBrand(String name, String brand);
}

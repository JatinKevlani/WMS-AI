package com.wmsai.repository;

import com.wmsai.entity.ProductBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductBundleRepository extends JpaRepository<ProductBundle, Integer> {
    List<ProductBundle> findByProductId(Integer productId);
}

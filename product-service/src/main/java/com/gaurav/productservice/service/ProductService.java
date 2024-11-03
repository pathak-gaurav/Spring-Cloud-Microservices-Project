package com.gaurav.productservice.service;

import com.gaurav.productservice.entity.Product;
import com.gaurav.productservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public boolean checkStockUpdateStock(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        return updateStock(product);
    }

    private boolean updateStock(Product product) {

        if (product.getStock() == 0 || product.getStock() < 1) {
            return false;
        }
        product.setStock(product.getStock() - 1);
        return true;
    }


}

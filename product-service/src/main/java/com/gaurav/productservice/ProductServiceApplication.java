package com.gaurav.productservice;

import com.gaurav.productservice.entity.Product;
import com.gaurav.productservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
public class ProductServiceApplication {

    private ProductRepository productRepository;

    public ProductServiceApplication(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @PostConstruct
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void loadDummyData() {

        Product[] products = {
                Product.builder().productName("Sunglasses").id(11L).price(new BigDecimal("18.99")).stock(74).build(),
                Product.builder().productName("Watch").id(12L).price(new BigDecimal("49.99")).stock(25).build(),
                Product.builder().productName("Headphones").id(13L).price(new BigDecimal("79.99")).stock(50).build(),
                Product.builder().productName("Smartphone Case").id(14L).price(new BigDecimal("9.99")).stock(100).build(),
                Product.builder().productName("Laptop Bag").id(15L).price(new BigDecimal("29.99")).stock(30).build(),
                Product.builder().productName("Tablet").id(16L).price(new BigDecimal("199.99")).stock(20).build(),
                Product.builder().productName("Smartwatch").id(17L).price(new BigDecimal("99.99")).stock(40).build(),
                Product.builder().productName("Power Bank").id(18L).price(new BigDecimal("19.99")).stock(60).build(),
                Product.builder().productName("Wireless Mouse").id(19L).price(new BigDecimal("14.99")).stock(80).build(),
                Product.builder().productName("Keyboard").id(20L).price(new BigDecimal("24.99")).stock(45).build()
        };

        Arrays.stream(products).forEach(productRepository::save);
    }
}

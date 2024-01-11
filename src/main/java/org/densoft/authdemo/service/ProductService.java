package org.densoft.authdemo.service;

import jakarta.annotation.PostConstruct;
import org.densoft.authdemo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    Random random = new Random();

    @PostConstruct
    public void generateProducts() {
        for (int i = 1; i <= 100; i++) {
            String price = String.format("%.2f", random.nextDouble(10, 200));
            Product product = new Product(i, "Product " + i, random.nextInt(10, 100), price);
            products.add(product);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProduct(long productId) {
        return products
                .stream()
                .filter(product -> product.getId() == productId)
                .findFirst().orElseThrow(() -> new RuntimeException("no product found with id: " + productId));
    }
}

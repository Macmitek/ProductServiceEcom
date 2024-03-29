package com.example.productservice.services;

import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id) throws InvalidProductIdException;

    List<Product> getAllProducts();

    Product updateProduct();

    Product replaceProduct(Long id, Product product);

    Product createProduct();

    void deleteProduct();
}

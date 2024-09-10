package com.example.productservice.services;

import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id) throws InvalidProductIdException;

//    List<Product> getAllProducts();
    Page<Product> getAllProducts(int pageNumber, int pageSize, String sortDir);


    Product updateProduct(Long id, Product product);

    Product replaceProduct(Long id, Product product);

    Product createProduct(Product product);

    void deleteProduct();
}

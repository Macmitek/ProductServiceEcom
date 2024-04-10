package com.example.productservice;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceApplicationTests {
    @Autowired // Dependency Injection
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    void contextLoads() {
    }
    @Test
    public void testQueries() {
//        List<ProductWithIdAndTitle> products = productRepository.someRandomQuery();
//
//        for (ProductWithIdAndTitle product : products) {
//            System.out.println(product.getId());
//            System.out.println(product.getTitle());
//        }

//        ProductWithIdAndTitle product = productRepository.doSomething(3L);
//        System.out.println(product.getId());
//        System.out.println(product.getTitle());
//
//        Product product1 = productRepository.doSomethingSQL();
//
//        Optional<Product> productOptional = productRepository.findById(2L);
//        Product product2 = null;
//        if (productOptional.isPresent()) {
//            product2 = productOptional.get();
//        }

//        System.out.println("DEBUG");

        //categoryRepository.deleteById(102L);

        Optional<Category> optionalCategory = categoryRepository.findById(2L);

        Category category = optionalCategory.get();

        System.out.println("Fetched Category");

        List<Product> products = category.getProducts();

        System.out.println("DEBUG");
    }
}

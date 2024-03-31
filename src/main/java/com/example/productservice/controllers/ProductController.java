package com.example.productservice.controllers;

import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    //localhost:8080/products/30
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws InvalidProductIdException {

    /* throw new RuntimeException("Something went wrong");
           Product product = null;
            try {
               product = productService.getProductById(id);
           } catch (RuntimeException e) {
                System.out.println("Something went wrong");
               return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);
            } catch (ArrayIndexOutOfBoundsException e) {
                return
           }
     */

        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //localhost:8080/products
    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    //create a Product
    @PostMapping("/")
    public Product createProduct(@RequestBody Product product) {
        return new Product();
    }

    //Partial Update.
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id,@RequestBody Product product) {
        System.out.println("inside updateproduct controller");
        return productService.updateProduct(id,product);
    }

    //Replace Product
    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id,@RequestBody Product product) {
        System.out.println("inside replace product controller");
        return productService.replaceProduct(id,product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {

    }
}

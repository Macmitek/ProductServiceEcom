package com.example.productservice.controllers;

import com.example.productservice.commons.AuthCommons;
import com.example.productservice.dtos.UserDto;
import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Product;
import com.example.productservice.dtos.Role;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private AuthCommons authCommons;
    private RestTemplate restTemplate;

    ProductController(@Qualifier("selfProductService") ProductService productService, AuthCommons authCommons, RestTemplate restTemplate) {
        this.productService = productService;
        this.authCommons = authCommons;
        this.restTemplate = restTemplate;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws InvalidProductIdException {

        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //localhost:8080/products
    @GetMapping("/all/{token}")
    public ResponseEntity<List<Product>> getAllProducts( @PathVariable String token) {
        System.out.println("inside all token  :: " + token);
        UserDto userDto = authCommons.validateToken(token);
        if(userDto == null){
            return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        boolean isAdmin = false;
//        System.out.println("isAdmin : " + isAdmin);

//        for(Role role : userDto.getRoles())
//        {
//           if(role.equals("ADMIN")){
//               isAdmin = true;
//               break;
//           }
//        }
//        if(!isAdmin){
//            return null;
//        }
        List<Product> products = productService.getAllProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //create a Product
    @PostMapping("/")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
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

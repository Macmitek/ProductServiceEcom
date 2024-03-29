package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;

    FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Product convertFakeStoreProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImage(fakeStoreProductDto.getImage());
        product.setPrice(fakeStoreProductDto.getPrice());

        Category category = new Category();
        category.setTitle(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }
    @Override
    public Product getProductById(Long id) throws InvalidProductIdException {
        //Call the FakeStore API to get the product with given ID here.
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);

        if (fakeStoreProductDto == null) {
            return null;
        }

        //Convert fakeStoreProductDto to product object.
        return convertFakeStoreProductDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtoArr =  restTemplate.getForObject("https://fakestoreapi.com/products/", FakeStoreProductDto[].class);
        if(fakeStoreProductDtoArr == null){
            return  null;
        }
        return convertFakeStoreProductDtoListToProductList(fakeStoreProductDtoArr);
    }

    private List<Product> convertFakeStoreProductDtoListToProductList(FakeStoreProductDto[] fakeStoreProductDtoArr) {
        List<Product> productList = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto: fakeStoreProductDtoArr){
            productList.add(convertFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }
        return productList;
    }
    @Override
    public Product updateProduct() {
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        //Put Method
        // replace the product with given id with input output
        // and return the updated product in output
         // Convert Product to FakeStoreProductDto
        FakeStoreProductDto mappedfakeStoreProductDto = convertProductToFakeStoreProductDto(product);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(mappedfakeStoreProductDto, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor<>(FakeStoreProductDto.class,
                restTemplate.getMessageConverters());
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, requestCallback, responseExtractor);
        return convertFakeStoreProductDtoToProduct(fakeStoreProductDto);

    }
    private FakeStoreProductDto convertProductToFakeStoreProductDto(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImage());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setCategory(product.getCategory().getTitle());
        return fakeStoreProductDto;
    }

    @Override
    public Product createProduct() {
        return null;
    }

    @Override
    public void deleteProduct() {

    }
}

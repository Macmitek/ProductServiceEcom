package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.exceptions.InvalidProductIdException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;
    private RedisTemplate redisTemplate;
    FakeStoreProductService(RestTemplate restTemplate,RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
//        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
//        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        this.redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    private Product convertFakeStoreProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImage(fakeStoreProductDto.getImage());
        product.setPrice(fakeStoreProductDto.getPrice());

//        Category category = new Category();
//        category.setTitle(fakeStoreProductDto.getCategory());
//        product.setCategory(category);
        return product;
    }
    @Override
    public Product getProductById(Long id) throws InvalidProductIdException {

        System.out.println("Got the request in Product Service");
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + id);

        if (product != null) {
            // Cache Hits
            System.out.println("inside cache hit");
            return product;
        }

        //Call the FakeStore API to get the product with given ID here.
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);

        if (fakeStoreProductDto == null) {
            throw new InvalidProductIdException(id, "Invalid productId passed");
        }
        product = convertFakeStoreProductDtoToProduct(fakeStoreProductDto);
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + id, product);

        //Convert fakeStoreProductDto to product object.
        return product;
    }


    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize, String sortDir) {
        FakeStoreProductDto[] fakeStoreProductDtos =
                restTemplate.getForObject("https://fakestoreapi.com/products/",
                        FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
            products.add(convertFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }

        return new PageImpl<>(products);
    }

    private List<Product> convertFakeStoreProductDtoListToProductList(FakeStoreProductDto[] fakeStoreProductDtoArr) {
        List<Product> productList = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto: fakeStoreProductDtoArr){
            productList.add(convertFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }
        return productList;
    }
    @Override
    public Product updateProduct( Long id, Product product) {

//        Map<String, Object> requestBody = convertProductToMap(product);
//        Map<String,Object> updatedProductres =  restTemplate.patchForObject("https://fakestoreapi.com/products/" + id,requestBody, Map.class);
//        assert updatedProductres != null;
//        System.out.println("map res :"+updatedProductres);
//        return convertMaptoProduct(updatedProductres);

        System.out.println("inside update product api :" + product.getDescription());
        RequestCallback requestCallback = restTemplate.httpEntityCallback(product, FakeStoreProductDto.class);
        System.out.println("requestcallback res :" + requestCallback);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor<>(FakeStoreProductDto.class,
                restTemplate.getMessageConverters());
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PATCH, requestCallback, responseExtractor);
        return convertFakeStoreProductDtoToProduct(fakeStoreProductDto);
    }

    private Product convertMaptoProduct(Map<String, Object> updatedProductres) {

        Product product = new Product();
        product.setId((Long) updatedProductres.get("id"));
        product.setTitle((String) updatedProductres.get("title"));
        product.setPrice((Double) updatedProductres.get("price"));
        product.setDescription((String) updatedProductres.get("description"));
        product.setImage((String) updatedProductres.get("image"));
        return product;
    }

    private Map<String, Object> convertProductToMap(Product product) {
        Map<String, Object> map = new HashMap<>();
        if (product.getTitle() != null) map.put("title", product.getTitle());
        if (product.getPrice() != 0.0) map.put("price", product.getPrice());
        if (product.getDescription() != null) map.put("description", product.getDescription());
        if (product.getImage() != null) map.put("image", product.getImage());
        if (product.getCategory() != null) map.put("category", product.getCategory().getTitle());
        return map;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        System.out.println("inside replace product !!");
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
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public void deleteProduct() {

    }
}

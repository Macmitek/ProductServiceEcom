package com.example.productservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
public class CategoryDto implements Serializable {
    Long id;
    Date createdAt;
    Date updatedAt;
    String title;
}

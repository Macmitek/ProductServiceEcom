package com.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExceptionDto {
    private Long productId;
    private String message;
}

package com.example.productservice.advices;

import com.example.productservice.dtos.ArithmeticExceptionDto;
import com.example.productservice.dtos.ArrayIndexOutOfBoundExceptionDto;
import com.example.productservice.dtos.ExceptionDto;
import com.example.productservice.exceptions.InvalidProductIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionhandlerAdvice {
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ArithmeticExceptionDto>  handleArithmeticException(){
        ArithmeticExceptionDto dto = new ArithmeticExceptionDto();
        dto.setMessage("Something went wrong");
        dto.setDetail("Some random detail");
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<ArrayIndexOutOfBoundExceptionDto> handleAIOBException() {
        return null;
    }
    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<ExceptionDto> handleInvalidProductIdException() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Invalid productId passed, please retry with a valid productId");
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }
}

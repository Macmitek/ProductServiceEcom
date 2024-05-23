package com.example.productservice.commons;

import com.example.productservice.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthCommons {
    private RestTemplate restTemplate;

    public AuthCommons(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
        public UserDto validateToken(String token) {
            //call userservice validate token api
            System.out.println("inside authenticate token product ::"+token);
            ResponseEntity<UserDto> responseEntity= restTemplate.postForEntity("http://localhost:4141/users/validate/" + token,null, UserDto.class);
            if(responseEntity.getBody() == null){
                return null;
            }
            return responseEntity.getBody();
        }
}

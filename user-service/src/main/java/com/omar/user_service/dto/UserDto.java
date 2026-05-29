package com.omar.user_service.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
}

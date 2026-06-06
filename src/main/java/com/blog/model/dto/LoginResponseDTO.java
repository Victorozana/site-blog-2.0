package com.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String name;
    private Long idUser;
    private String userType;
}

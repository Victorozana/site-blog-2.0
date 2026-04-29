package com.blog.model.dto;

import com.blog.model.UserType;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class UserRegistrationDTO {
    private String name;
    private String lastname;
    private LocalDate dtNasc;
    private String fone;
    private String email;
    private String cryptographyPassword;
    private UserType userType;
}

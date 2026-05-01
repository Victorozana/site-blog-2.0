package com.blog.model.entity;

import com.blog.model.UserType;
import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
public class User extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column (nullable = false
    )
    private String lastname;
    @Column(nullable = false, name = "dt_nasc")
    private LocalDate dtNasc;
    @Column(nullable = false, length = 20)
    private String fone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, name = "cryptography_password")
    private String cryptographyPassword;
    @Column(nullable = false, name = "user_type")
    private UserType userType;
    @Column(nullable = false, name = "update_dateTime")
    private LocalDateTime updateDateTime;
    @Column(nullable = false, name = "created_dateTime")
    private LocalDateTime createdDateTime;

    public User(UserRegistrationDTO dto){
        this.name = dto.getName();
        this.lastname = dto.getLastname();
        this.dtNasc = dto.getDtNasc();
        this.fone = dto.getFone();
        this.email = dto.getEmail();
        this.cryptographyPassword = dto.getCryptographyPassword();
    }

    public User(UserLoginDTO dto){
        this.email = dto.getEmail();
        this.cryptographyPassword = dto.getCryptographyPassword();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = LocalDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDtNasc(LocalDate dtNasc) {
        this.dtNasc = dtNasc;
    }


    public void setFone(String fone) {
        this.fone = fone;
    }

    public void setCryptographyPassword(String cryptographyPassword) {
        this.cryptographyPassword = cryptographyPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @PrePersist
    private void dateRegister() {
        this.createdDateTime = LocalDateTime.now();
        this.updateDateTime = LocalDateTime.now();
    }
}
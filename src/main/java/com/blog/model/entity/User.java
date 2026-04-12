package com.blog.model.entity;

import com.blog.model.UserType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_users")
public class User extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(LocalDate dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getCryptographyPassword() {
        return cryptographyPassword;
    }

    public void setCryptographyPassword(String cryptographyPassword) {
        this.cryptographyPassword = cryptographyPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
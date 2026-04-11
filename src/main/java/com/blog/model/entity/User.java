package com.blog.model.entity;

import com.blog.model.UserType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate dt_nasc;
    @Column(nullable = false)
    private String fone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String cryptography_password;
    @Column(nullable = false)
    private UserType user_type;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDt_nasc() {
        return dt_nasc;
    }

    public void setDt_nasc(LocalDate dt_nasc) {
        this.dt_nasc = dt_nasc;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getcryptography_password() {
        return cryptography_password;
    }

    public void setcryptography_password(String cryptography_password) {
        this.cryptography_password = cryptography_password;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
    }
}

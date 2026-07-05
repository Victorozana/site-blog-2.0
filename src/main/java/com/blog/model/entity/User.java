package com.blog.model.entity;

import com.blog.model.category.UserType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column (nullable = false)
    private String lastname;
    @Column(nullable = false, name = "dt_nasc")
    private LocalDate dtNasc;
    @Column(nullable = false, length = 20)
    private String fone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, name = "cryptography_password")
    private String cryptographyPassword;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "user_type")
    private UserType userType;
    @Column(nullable = false, name = "update_dateTime")
    private LocalDateTime updateDateTime;
    @Column(nullable = false, name = "created_dateTime")
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdDateTime;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    private String bio;

    @PrePersist
    private void dateRegister() {
        this.createdDateTime = LocalDateTime.now();
        this.updateDateTime = LocalDateTime.now();
    }
}

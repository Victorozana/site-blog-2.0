package com.blog.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @JoinColumn(name = "blog_id")
    private Blog blog;
}

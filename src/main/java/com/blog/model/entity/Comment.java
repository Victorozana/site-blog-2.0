package com.blog.model.entity;

import com.blog.model.dto.CommentRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;
}

package com.blog.model.entity;

import com.blog.model.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String title;
    private String subtitle;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Category category;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime localDateTime;

    public void setTitle(String title) {
        this.title = title;
    }


    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    private void setLocalDateTime(){
        this.localDateTime = LocalDateTime.now();
    }
}

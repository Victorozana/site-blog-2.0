package com.blog.model.entity;

import com.blog.model.category.Category;
import com.blog.model.dto.BlogScreenDTO;
import com.blog.model.dto.MainScreenDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
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
    @Setter(AccessLevel.NONE)
    private LocalDateTime localDateTime;

    public Blog(MainScreenDTO dto){
        this.id = dto.getId();
        this.description = dto.getDescription();
        this.subtitle = dto.getSubtitle();
        this.title = dto.getTitle();
        this.localDateTime = dto.getLocalDateTime();
    }

    public Blog(BlogScreenDTO dto){
        this.title = dto.getTitle();
        this.subtitle = dto.getSubtitle();
        this.description = dto.getDescription();
    }

    @PrePersist
    private void setLocalDateTime(){
        this.localDateTime = LocalDateTime.now();
    }
}

package com.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private String action;
    private Long userId;
    private String userName;
    private String userType;
    private String details;
    private LocalDateTime createdAt;
}

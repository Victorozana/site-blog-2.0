package com.blog.dao;

import com.blog.model.entity.AuditLog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuditLogDAO implements PanacheRepository<AuditLog> {
}

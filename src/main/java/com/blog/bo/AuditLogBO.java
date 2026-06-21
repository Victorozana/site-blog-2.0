package com.blog.bo;

import com.blog.dao.AuditLogDAO;
import com.blog.model.dto.AuditLogDTO;
import com.blog.model.entity.AuditLog;
import com.blog.model.entity.User;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AuditLogBO {
    @Inject
    AuditLogDAO auditLogDAO;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void log(String action, User user, String details) {
        Long userId = user != null ? user.getId() : null;
        String userName = user != null ? user.getName() : null;
        String userType = user != null && user.getUserType() != null ? user.getUserType().name() : null;

        log(action, userId, userName, userType, details);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void log(String action, Long userId, String userName, String userType, String details) {
        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .userId(userId)
                .userName(userName)
                .userType(userType)
                .details(details)
                .build();

        auditLogDAO.persist(auditLog);
    }

    public List<AuditLogDTO> listRecent(int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 200));

        return auditLogDAO.find("order by createdAt desc")
                .page(Page.of(0, safeLimit))
                .list()
                .stream()
                .map(log -> new AuditLogDTO(
                        log.getId(),
                        log.getAction(),
                        log.getUserId(),
                        log.getUserName(),
                        log.getUserType(),
                        log.getDetails(),
                        log.getCreatedAt()
                ))
                .toList();
    }
}

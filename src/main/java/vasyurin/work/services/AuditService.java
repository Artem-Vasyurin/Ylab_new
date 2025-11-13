package vasyurin.work.services;

import vasyurin.work.dto.User;

public interface AuditService {
    void log(User user, String action);
}

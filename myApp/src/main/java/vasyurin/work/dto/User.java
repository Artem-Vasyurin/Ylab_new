package vasyurin.work.dto;

import aspectsAudit.AuditableUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vasyurin.work.enams.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements AuditableUser {
    private String username;
    private String password;
    private UserRole role;
    private String token;

    @Override
    public String getUsername() {
        return username;
    }

}
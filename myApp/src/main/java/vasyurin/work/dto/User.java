package vasyurin.work.dto;

import auditaspect.aspectsAudit.AuditableUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vasyurin.work.enams.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements AuditableUser {
    @Schema(description = "Логин пользователя", example = "admin")
    private String username;

    @Schema(description = "Пароль пользователя", example = "admin123")
    private String password;

    @Schema(description = "Роль пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole role;

    @Schema(description = "JWT токен", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String token;
}
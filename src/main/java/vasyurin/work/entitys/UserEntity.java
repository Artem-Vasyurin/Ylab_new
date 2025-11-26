package vasyurin.work.entitys;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String token;
}

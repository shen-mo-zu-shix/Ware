package org.example.ware.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String avatar;
    private Integer  role;
    // 不包含密码等敏感信息
}
package org.example.ware.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String oldPassword; // 原密码
    private String newPassword; // 新密码
}

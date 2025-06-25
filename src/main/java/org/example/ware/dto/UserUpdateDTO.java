package org.example.ware.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;    // 用户名
    private String email;       // 邮箱
    private String avatar;      // 头像URL
    private String introduction;// 个人简介
}

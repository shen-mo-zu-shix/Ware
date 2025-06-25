package org.example.ware.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.ware.component.JWTComponent;
import org.example.ware.dto.*;
import org.example.ware.entity.User;
import org.example.ware.exception.Code;
import org.example.ware.service.IUserService;
import org.example.ware.vo.ResultVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.example.ware.exception.Code.*;


@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final IUserService IuserService;
    private final PasswordEncoder passwordEncoder;
    private final JWTComponent jWTComponent;
    //普通用户注册
    @PostMapping("/register")
    public ResultVO register(@RequestBody UserRegisterDTO registerDTO) {
        // 1. 参数校验
        if (StringUtils.isEmpty(registerDTO.getUsername()) ||
                StringUtils.isEmpty(registerDTO.getPassword()) ||
                StringUtils.isEmpty(registerDTO.getEmail())) {
            return ResultVO.error(PARAM_ERROR);
        }
        // 2. 校验用户名是否已存在
        User existingUser = IuserService.getUserByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            return ResultVO.error(NAME_ERROR);
        }
        // 3. 校验邮箱是否已注册
        User userByEmail = IuserService.getUserByEmail(registerDTO.getEmail());
        if (userByEmail != null) {
            return ResultVO.error(EMAIL_EXIST_ERROR);
        }
        // 4. 密码加密
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        // 5. 构建用户对象
        User newUser = User.builder()
                .username(registerDTO.getUsername())
                .password(encodedPassword)
                .email(registerDTO.getEmail())
                .emailVerified(false) // 默认邮箱未验证
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        // 6. 保存用户
        IuserService.saveUser(newUser);
        // 7. 生成 Token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", newUser.getUserId());
        payload.put("username", newUser.getUsername());
        String token = jWTComponent.encode(payload);

        // 7. 返回成功信息
        Map<String, Object> result = new HashMap<>();
        result.put("userId", newUser.getUserId());
        result.put("username", newUser.getUsername());
        result.put("token", token);
        result.put("message", "注册成功");
        return ResultVO.success(result);
    }
    //普通用户登录
    @PostMapping("login")
    public ResultVO login(@RequestBody UserLoginDTO loginDTO) {
        // 1. 参数校验
        if (StringUtils.isEmpty(loginDTO.getUsername()) ||
                StringUtils.isEmpty(loginDTO.getPassword())) {
            return ResultVO.error(NAMEPASS_ERROR);
        }
        // 2. 根据用户名查询用户
        User user = IuserService.getUserByUsername(loginDTO.getUsername());
        // 3. 密码校验
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResultVO.error(LOGIN_ERROR);
        }

//        // 4. 邮箱未验证（可选校验，根据业务需求）
//        if (!user.getEmailVerified()) {
//            return ResultVO.error(EMAIL_NOT_VERIFIED_ERROR);
//        }

        // 5. 生成 JWT Token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("username", user.getUsername());
        String token = jWTComponent.encode(payload);

        // 6. 构建返回数据（脱敏处理，不返回敏感信息）
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("user", userDTO);
        result.put("token", token);

        return ResultVO.success(result);
    }
    //用户更改密码
    @PutMapping("change-password")
    public ResultVO changePassword(
            @RequestBody PasswordDTO dto,
            @RequestHeader("Authorization") String token) {

        // 1. 解析Token获取用户ID
        try {
            DecodedJWT decodedJWT = jWTComponent.decode(token);
            Long userId = decodedJWT.getClaim("userId").asLong();
            User user = IuserService.getById(userId);

            // 2. 校验原密码
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                return ResultVO.error(PASSAGE_ERROR);
            }
            // 3. 密码强度校验
            if (!dto.getNewPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                return ResultVO.error(PASSAGE_REQUEST);
            }

            // 4. 更新密码
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            user.setUpdateTime(LocalDateTime.now());
            IuserService.updateById(user);

            return ResultVO.success("密码修改成功，请重新登录");

        } catch (Exception e) {
            return ResultVO.error(Code.valueOf("登录状态失效，请重新登录"));
        }
    }
    //用户更改信息
    @PutMapping("updateinformation")
    public ResultVO updateUserInfo(
            @RequestBody UserUpdateDTO dto,
            @RequestHeader("Authorization") String token) {

        try {
            // 1. 解析Token获取用户ID
            DecodedJWT decodedJWT = jWTComponent.decode(token);
            Long userId = decodedJWT.getClaim("userId").asLong();
            User user = IuserService.getById(userId);

            // 2. 校验用户名唯一性（修改时排除自身）
            if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
                User existUser = IuserService.getByUsername(dto.getUsername());
                if (existUser != null) {
                    return ResultVO.error(Code.valueOf("用户名已存在"));
                }
                user.setUsername(dto.getUsername());
            }

            // 3. 校验邮箱唯一性（同上）
            if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
                User existUser = IuserService.getUserByEmail(dto.getEmail());
                if (existUser != null) {
                    return ResultVO.error(Code.valueOf("邮箱已被注册"));
                }
                user.setEmail(dto.getEmail());
                user.setEmailVerified(false); // 重置验证状态
            }

            // 4. 更新其他信息
            if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
            if (dto.getIntroduction() != null) user.setIntroduction(dto.getIntroduction());

            user.setUpdateTime(LocalDateTime.now());
            IuserService.updateById(user);

            return ResultVO.success("更新成功");

        } catch (Exception e) {

            return ResultVO.error(Code.valueOf("操作失败，请重试"));
        }
    }
}

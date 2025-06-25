package org.example.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.example.ware.entity.User;


public interface IUserService extends IService<User> {
    //根据用户名查用户
    User getUser(Long userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void saveUser(User user);

    User getByUsername(String username);

}

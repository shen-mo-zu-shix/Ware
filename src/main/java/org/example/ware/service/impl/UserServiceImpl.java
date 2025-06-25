package org.example.ware.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.ware.entity.User;
import org.example.ware.mapper.UserMapper;
import org.example.ware.service.IUserService;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private Result result;
    private UserMapper userMapper;
    @Override
    public User getUser(Long userId){

        return userMapper.selectById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    @Override
    public void saveUser(User user) {
        userMapper.insert(user);
    }
    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(
                new QueryWrapper<User>().eq("username", username));
    }
}

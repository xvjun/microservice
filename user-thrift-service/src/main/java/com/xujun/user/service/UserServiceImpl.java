package com.xujun.user.service;

import com.xujun.thrift.user.UserInfo;
import com.xujun.thrift.user.UserService;
import com.xujun.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService.Iface {

    @Resource
    private UserMapper userMapper;

    

    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getUserByName(String name) throws TException {
        return userMapper.getUserByName(name);
    }

    @Override
    public void regiserUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}

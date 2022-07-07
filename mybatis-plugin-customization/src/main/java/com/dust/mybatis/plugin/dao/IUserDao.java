package com.dust.mybatis.plugin.dao;

import com.dust.mybatis.plugin.pojo.User;

import java.io.IOException;
import java.util.List;

public interface IUserDao {

    //查询所有用户
    public List<User> findAll() throws IOException;



}

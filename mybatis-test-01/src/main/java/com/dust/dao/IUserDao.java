package com.dust.dao;

import com.dust.pojo.User;

import java.util.List;

/**
 * @author DUST
 * @description
 * @date 2022/6/1
 */
public interface IUserDao {

    //查询所有用户
    public List<User> findAll() throws Exception;

    //根据条件进行用户查询
    public User findByCondition(User user) throws Exception;

    //查询指定用户
    User selectOne(User user);
}

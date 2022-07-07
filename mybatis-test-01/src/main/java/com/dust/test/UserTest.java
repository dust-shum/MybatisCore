package com.dust.test;

import com.dust.dao.IUserDao;
import com.dust.io.Resources;
import com.dust.pojo.User;
import com.dust.sqlSession.SqlSession;
import com.dust.sqlSession.SqlSessionFactory;
import com.dust.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

/**
 * @author DUST
 * @description
 * @date 2022/6/1
 */
public class UserTest {

    @Test
    public void test() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");

        //mapper.xml 的 namespace要为 User
//        User user2 = sqlSession.selectOne("User.selectOne", user);
//
//        System.out.println(user2);
//
//        List<User> users = sqlSession.selectList("User.selectList");
//        for (User user1 : users) {
//            System.out.println(user1);
//        }

        //---以上都是statement硬编码，现在改成代理对象调用
        //mapper.xml 的 namespace要为com.dust.dao.IUserDao
        IUserDao userMapper = sqlSession.getMapper(IUserDao.class);
        User user1 = userMapper.selectOne(user);
        System.out.println(user1);

//        List<User> all = userDao.findAll();
//        for (User user1 : all) {
//            System.out.println(user1);
//        }

    }
}

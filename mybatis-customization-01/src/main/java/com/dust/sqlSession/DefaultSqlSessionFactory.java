package com.dust.sqlSession;

import com.dust.pojo.Configuration;

/**
 * @author DUST
 * @description
 * @date 2022/5/25
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}

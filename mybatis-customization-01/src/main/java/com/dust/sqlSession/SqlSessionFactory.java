package com.dust.sqlSession;

/**
 * @author DUST
 * @description sql会话工厂接口
 * @date 2022/5/25
 */
public interface SqlSessionFactory {

    public SqlSession openSession();
}

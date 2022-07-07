package com.dust.sqlSession;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DUST
 * @description sql会话
 * @date 2022/5/25
 */
public interface SqlSession{

    public <E> List<E> selectList(String statementId, Object... param) throws Exception;

    public <T> T selectOne(String statementId,Object... params) throws Exception;

    public void close() throws SQLException;

    public <T> T getMapper(Class<?> mapperClass);
}

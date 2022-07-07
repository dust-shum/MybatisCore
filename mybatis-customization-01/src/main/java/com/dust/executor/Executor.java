package com.dust.executor;

import com.dust.pojo.Configuration;
import com.dust.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DUST
 * @description sql执行器接口
 * @date 2022/5/25
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception;

    void close() throws SQLException;
}

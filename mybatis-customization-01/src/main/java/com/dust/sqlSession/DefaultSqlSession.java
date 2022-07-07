package com.dust.sqlSession;

import com.dust.executor.Executor;
import com.dust.executor.SimpleExecutor;
import com.dust.pojo.Configuration;
import com.dust.pojo.MappedStatement;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author DUST
 * @description
 * @date 2022/5/25
 */
public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;

    //sql处理器
    private Executor executor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public <E> List<E> selectList(String statementId, Object... param) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<E> query = executor.query(configuration, mappedStatement, param);
        return query;
    }

    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> objects = selectList(statementId, params);
        if(objects.size() == 1){
            return objects.get(0);
        }else{
            throw new RuntimeException("返回结果过多");
        }
    }

    public void close() throws SQLException {
        executor.close();
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        T proxyClass = (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass},
                new InvocationHandler(){
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //获取代理方法  selectOne
                        String methodName = method.getName();
                        //获取类全名 className:namespace
                        String className = method.getDeclaringClass().getName();
                        //获取statementId
                        String statementId = className + "." + methodName;
                        //获取对应的映射信息
                        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                        //获取方法的出参类型
                        Type genericReturnType = method.getGenericReturnType();
                        //判断是否实现泛型类型参数化
                        if(genericReturnType instanceof ParameterizedType){
                            List<Object> objects = selectList(statementId, args);
                            return objects;
                        }
                        return selectOne(statementId,args);
                    }
                });
        return proxyClass;
    }
}

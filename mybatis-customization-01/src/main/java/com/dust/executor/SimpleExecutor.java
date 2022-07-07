package com.dust.executor;

import com.dust.pojo.BoundSql;
import com.dust.pojo.Configuration;
import com.dust.pojo.MappedStatement;
import com.dust.pojo.ParameterMapping;
import com.dust.utils.GenericTokenParser;
import com.dust.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DUST
 * @description
 * @date 2022/5/25
 */
public class SimpleExecutor implements Executor{

    private Connection connection;

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception {
        //获取数据库连接
        connection = configuration.getDataSource().getConnection();
        //对sql进行处理
        // select * from user where id = #{id} and username = #{username}
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        // select * from user where id = ? and username = ?
        String finalSql = boundSql.getSqlText();
        //获取入参
        Class<?> parameterType = mappedStatement.getParameterType();
        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
        //获取所有入参
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        //遍历所有入参加入预编译对象
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String name = parameterMapping.getName();
            //反射字段
            Field declaredField = parameterType.getDeclaredField(name);
            declaredField.setAccessible(true);
            //字段的值
            Object o = declaredField.get(param[0]);
            //给占位符赋值
            preparedStatement.setObject(i+1,o);
        }

        //执行查询
        ResultSet resultSet = preparedStatement.executeQuery();
        //获取返回的类型
        Class<?> resultType = mappedStatement.getResultType();
        //存放结果
        ArrayList<E> results = new ArrayList<E>();
        //处理执行结果
        while(resultSet.next()){
            E o = (E) resultType.newInstance();
            //获取结果
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取属性数量
            int columnCount = metaData.getColumnCount();
            //处理每一行的属性
            for (int i = 1; i < columnCount; i++) {
                //属性名
                String columnName = metaData.getColumnName(i);
                //属性值
                Object value = resultSet.getObject(columnName);
                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                //创建属性描述器，为属性生成读写方法
                PropertyDescriptor propertyDescriptor;
                try{
                    propertyDescriptor = new PropertyDescriptor(columnName, resultType);
                }catch (Exception exception){
                    continue;
                }
                //获取写方法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                //向类中写入值
                writeMethod.invoke(o,value);
            }
            results.add(o);

        }
        //返回结果
        return results;
    }

    public void close() throws SQLException {
        connection.close();
    }

    /**
     * @author DUST
     * @description 获取处理过后的sql
     * @date 2022/5/25
     * @param sql
     * @return com.dust.pojo.BoundSql
    */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：主要是配合通⽤标记解析器GenericTokenParser类完成对配置⽂件等的解析⼯作，其中TokenHandler主要完成处理
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //GenericTokenParser :通⽤的标记解析器，完成了代码⽚段中的占位符的解析，
        // 然后再根据给定的标记处理器(TokenHandler)来进⾏表达式的处理
        //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记处 理器)
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String parsedSQL = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings =
                parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parsedSQL, parameterMappings);
        return boundSql;
    }
}

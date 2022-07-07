package com.dust.pojo;

/**
 * @author DUST
 * @description 映射声明
 * @date 2022/5/25
 */
public class MappedStatement {

    //编号
    private String id;
    //sql语句
    private String sql;
    //输入参数
    private Class<?> parameterType;
    //输出参数
    private Class<?> resultType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }
}

package com.dust.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DUST
 * @description 存放配置信息
 * @date 2022/5/25
 */
public class Configuration {

    //数据源
    private DataSource dataSource;

    //map集合： key:statementId value:MappedStatement
    private Map<String,MappedStatement> mappedStatementMap = new HashMap<String,MappedStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}

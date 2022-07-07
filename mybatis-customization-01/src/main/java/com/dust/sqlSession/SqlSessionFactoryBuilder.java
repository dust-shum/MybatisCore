package com.dust.sqlSession;

import com.dust.pojo.Configuration;
import com.dust.pojo.XMLConfigBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author DUST
 * @description sql会话工厂建造类
 * @date 2022/5/25
 */
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException, ClassNotFoundException {
        //1.解析配置文件，将数据存放到 Configuration
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(configuration);
        Configuration configuration = xmlConfigBuilder.parseConfiguration(inputStream);

        //2.创建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }
}

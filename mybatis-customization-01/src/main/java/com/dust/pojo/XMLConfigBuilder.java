package com.dust.pojo;

import com.dust.io.Resources;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author DUST
 * @description xml配置建造类
 * @date 2022/5/25
 */
public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @author DUST
     * @description 将配置文件的输入流信息转化为javaBean放到configuration对象中
     * @date 2022/5/25
     * @param inputStream xml配置文件的输入流
     * @return com.dust.pojo.Configuration
    */
    public Configuration parseConfiguration(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException {
        //使⽤dom4j解析xml配置⽂件，将解析出来的内容封装到Configuration和MappedStatement中
        Document document = new SAXReader().read(inputStream);
        //获取<configuration>
        Element rootElement = document.getRootElement();
        //获取<property>
        List<Element> propertyElements = rootElement.selectNodes("property");
        //获取<property>里面的属性
        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.setProperty(name,value);
        }
        //获取连接池
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //设置数据源
        configuration.setDataSource(comboPooledDataSource);
        //设置mapper
        List<Element> mapperElements = rootElement.selectNodes("mapper");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        //将所有的映射器内容加载到本地内存中
        for (Element mapperElement : mapperElements) {
            String mapperPath = mapperElement.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
            //将映射器内容加载到本地内存中
            xmlMapperBuilder.parseMapper(resourceAsStream);
        }
        return configuration;
    }
}

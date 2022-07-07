package com.dust.pojo;

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
 * @description xml映射器建造类
 * @date 2022/5/25
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @author DUST
     * @description 将配置文件的输入流信息转化为javaBean放到configuration对象中
     * @date 2022/5/25
     * @param inputStream xml配置文件的输入流
     * @return com.dust.pojo.Configuration
    */
    public void parseMapper(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        //使⽤dom4j解析xml配置⽂件，将解析出来的内容封装到Configuration和MappedStatement中
        Document document = new SAXReader().read(inputStream);
        //获取<mapper>
        Element rootElement = document.getRootElement();
        //获取<mapper>的值
        String namespace = rootElement.attributeValue("namespace");
        //获取<select>
        List<Element> selects = rootElement.selectNodes("select");
        for (Element element : selects) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");

            Class<?> parameterTypeClass = null;
            if(parameterType != null){
                parameterTypeClass = getClassType(parameterType);
            }
            Class<?> resultTypeClass = getClassType(resultType);
            //statement id
            String key = namespace+"."+id;
            //sql语句
            String sqlText = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(resultTypeClass);
            mappedStatement.setSql(sqlText);

            //将数据丢进本地内存
            configuration.getMappedStatementMap().put(key,mappedStatement);

        }
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        return Class.forName(parameterType);
    }
}

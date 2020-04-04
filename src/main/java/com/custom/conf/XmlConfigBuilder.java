package com.custom.conf;

import com.custom.batis.Resource;
import com.custom.model.Configuration;
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
 * @Author: 刘劲
 * @Date: 2020/3/28 22:04
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder() {
        this.configuration = new Configuration();
    }

    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> nodes = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element e : nodes){
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            properties.setProperty(name, value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driver"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
        for (Element e : mapperElements){
            String path = e.attributeValue("path");
            xmlMapperBuilder.parseConfig(Resource.getResource(path));
        }
        return configuration;
    }
}

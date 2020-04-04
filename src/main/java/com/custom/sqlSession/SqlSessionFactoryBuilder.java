package com.custom.sqlSession;

import com.custom.conf.XmlConfigBuilder;
import com.custom.model.Configuration;
import java.io.InputStream;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:02
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory builder(InputStream inputStream) throws Exception {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }
}

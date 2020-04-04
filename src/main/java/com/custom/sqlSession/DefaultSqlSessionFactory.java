package com.custom.sqlSession;

import com.custom.model.Configuration;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:02
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaulSqlSession(configuration);
    }
}

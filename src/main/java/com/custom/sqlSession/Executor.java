package com.custom.sqlSession;

import com.custom.model.Configuration;
import com.custom.model.MappedStatment;

import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:22
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception;
    int update(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception;
    boolean delete(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception;
    boolean insert(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception;
}

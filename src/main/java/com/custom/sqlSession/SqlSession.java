package com.custom.sqlSession;

import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:08
 */
public interface SqlSession {

    <E> List<E> selectList(String statementid, Object... params) throws Exception;

    <T> T selectOne(String statementid,Object... params) throws Exception;

    <T> T getMapper(Class<?> mapperClass);

    int update(String statementId, Object... params) throws Exception;
    boolean delete(String statementId, Object... params) throws Exception;
    boolean insert(String statementId, Object... params) throws Exception;
}

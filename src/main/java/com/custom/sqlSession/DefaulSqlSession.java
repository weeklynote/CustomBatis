package com.custom.sqlSession;

import com.custom.model.Configuration;
import com.custom.model.MappedStatment;
import com.mysql.jdbc.StringUtils;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:08
 */
public class DefaulSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaulSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new SimpleExecutor();
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {
        Map<String, MappedStatment> configurationStatment = configuration.getStatment();
        MappedStatment statment = configurationStatment.get(statementid);
        List<Object> objects = executor.query(configuration, statment, params);
        return (List<E>) objects;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        List<Object> objects = selectList(statementid, params);
        if (objects != null && objects.size() > 0) {
            if (objects.size() == 1) {
                return (T) objects.get(0);
            } else {
                throw new IllegalArgumentException("查询到多个对象");
            }
        }
        return null;
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;
                // 准备参数2：params:args
                MappedStatment statment = configuration.getStatment().get(statementId);
                String commandType = statment.getCommandType();
                if (!StringUtils.isNullOrEmpty(commandType)) {
                    switch (commandType) {
                        case "select":
                            // 获取被调用方法的返回值类型
                            Type genericReturnType = method.getGenericReturnType();
                            // 判断是否进行了 泛型类型参数化
                            if (genericReturnType instanceof ParameterizedType) {
                                List<Object> objects = selectList(statementId, args);
                                return objects;
                            }
                            return selectOne(statementId, args);
                        case "update":
                            return update(statementId, args);
                        case "delete":
                            return delete(statementId, args);
                        case "insert":
                            return insert(statementId, args);
                        default:
                            throw new IllegalArgumentException("SQL语句不合法!");
                    }
                }
                return null;
            }
        });
        return (T) proxyInstance;
    }



    @Override
    public int update(String statementId, Object... params) throws Exception {
        Map<String, MappedStatment> configurationStatment = configuration.getStatment();
        MappedStatment statment = configurationStatment.get(statementId);
        return executor.update(configuration, statment, params);
    }

    @Override
    public boolean delete(String statementId, Object... params) throws Exception {
        Map<String, MappedStatment> configurationStatment = configuration.getStatment();
        MappedStatment statment = configurationStatment.get(statementId);
        return executor.delete(configuration, statment, params);
    }

    @Override
    public boolean insert(String statementId, Object... params) throws Exception {
        Map<String, MappedStatment> configurationStatment = configuration.getStatment();
        MappedStatment statment = configurationStatment.get(statementId);
        return executor.insert(configuration, statment, params);
    }
}

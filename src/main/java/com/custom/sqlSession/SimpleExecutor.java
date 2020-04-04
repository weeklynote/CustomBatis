package com.custom.sqlSession;

import com.custom.conf.BoundSql;
import com.custom.model.Configuration;
import com.custom.model.MappedStatment;
import com.custom.utils.GenericTokenParser;
import com.custom.utils.ParameterMapping;
import com.custom.utils.ParameterMappingTokenHandler;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:23
 */
public class SimpleExecutor implements Executor {

    private PreparedStatement db(Configuration configuration, MappedStatment mappedStatement, Object... params)  throws Exception {
        // 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        // 3.获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
        String paramterType = mappedStatement.getParameterType();
        Class<?> paramtertypeClass = getClassType(paramterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, o);
        }
        return preparedStatement;
    }

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception {
        final PreparedStatement preparedStatement = db(configuration, mappedStatement, params);
        // 5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()){
            Object o =resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);
                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }

    @Override
    public int update(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception {
        final PreparedStatement preparedStatement = db(configuration, mappedStatement, params);
        return preparedStatement.executeUpdate();
    }

    @Override
    public boolean delete(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception {
        final PreparedStatement preparedStatement = db(configuration, mappedStatement, params);
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean insert(Configuration configuration, MappedStatment mappedStatement, Object... params) throws Exception {
        final PreparedStatement preparedStatement = db(configuration, mappedStatement, params);
        return preparedStatement.executeUpdate() > 0;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType != null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;
    }

    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parseSql,parameterMappings);
        return boundSql;
    }
}

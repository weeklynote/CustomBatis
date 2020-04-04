package com.custom.conf;

import com.custom.utils.ParameterMapping;

import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 23:34
 */
public class BoundSql {

    private String sql;
    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String sql, List<ParameterMapping> parameterMappingList) {
        this.sql = sql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}

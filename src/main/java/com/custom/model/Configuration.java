package com.custom.model;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 21:58
 */
public class Configuration {

    private DataSource dataSource;
    private Map<String, MappedStatment> statment = new HashMap<>(16);

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatment> getStatment() {
        return statment;
    }

    public void setStatment(Map<String, MappedStatment> statment) {
        this.statment = statment;
    }
}

package com.custom.jdbc;

import com.custom.model.User;
import com.custom.utils.IOUtils;

import java.sql.*;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 21:51
 */
public class Jdbc {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 加载数据库驱动
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 通过驱动管理类获取数据库链接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/note?characterEncoding=utf-8", "root", "lj123456");
            // 定义sql语句？表示占位符
            String sql = "select * from user where id = ? and name = ?";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, "1");
            preparedStatement.setString(2, "liujin");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                User user = new User();
                user.setId(id);
                user.setName(name);
                System.err.println(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

package com.custom.model;

import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/29 22:26
 */
public interface UserDao {

    List<User> selectList();
    User selectByUser(User user);
    int updateById(User user);
    boolean deleteById(User user);
    boolean insert(User user);


}

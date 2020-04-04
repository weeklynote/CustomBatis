package com.custom;

import com.custom.batis.Resource;
import com.custom.model.User;
import com.custom.model.UserDao;
import com.custom.sqlSession.SqlSession;
import com.custom.sqlSession.SqlSessionFactory;
import com.custom.sqlSession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: 刘劲
 * @Date: 2020/3/29 0:05
 */
public class CustomBatisTest {

    /**
     * www.mybatis.org/mybatis-3/
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        InputStream inputStream = Resource.getResource("sqlConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().builder(inputStream);
        // 传统方式
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setId(1);
        user.setName("liujin");
        User result = sqlSession.selectOne("com.custom.model.UserDao.selectByUser", user);
        System.err.println(result);
        List<User> userList = sqlSession.selectList("com.custom.model.UserDao.selectList");
        for (User user1 : userList) {
            System.err.println(user1);
        }
        System.err.println("====================================");
        // 代理模式
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> users = userDao.selectList();
        for (User user1 : users) {
            System.err.println(user1);
        }
        // 查询
        User user1 = userDao.selectByUser(user);
        System.err.println(user1);
        System.err.println("===================================================");
        user.setName("2020-03-05");
        // 更新
        final int i = userDao.updateById(user);
        System.err.println(i);
        System.err.println("=========================");
        // 删除
        boolean suc = userDao.deleteById(user);
        System.err.println(suc);
        System.err.println("===================================");
        // 插入
        user = new User();
        user.setId(11111);
        user.setName("202020200202");
        System.err.println(userDao.insert(user));
    }
}

package com.custom.batis;

import java.io.InputStream;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 17:54
 */
public class Resource {

    public static InputStream getResource(String path){
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }
}

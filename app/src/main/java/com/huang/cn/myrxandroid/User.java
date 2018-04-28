package com.huang.cn.myrxandroid;

/**
 * {此处填写描述信息}
 * Created by huangchaobo on 2018/4/28 14.
 * 邮箱：huangchaobo@miao.cn
 */

public class User {
    String name;
    int age;

    public User(String huang, int i) {
        name=huang;
        age=i;
    }
    public User() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

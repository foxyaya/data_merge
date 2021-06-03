package cn.ybx66.shiro.service;


import cn.ybx66.userapi.pojo.User;

public interface LoginService{

    User getUserByName(String userName);
}

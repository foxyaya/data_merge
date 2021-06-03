package cn.ybx66.userservice.service;

import cn.ybx66.userapi.pojo.User;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 13:45
 * @description
 */
public interface UserService {

    String saveUser(User user);

    int update(User user);

    User getUser(String userName);

    User getUser(String userName,String password);
}

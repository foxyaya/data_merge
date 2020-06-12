package cn.ybx66.item.service;

import cn.ybx66.item.pojo.User;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/20 22:51
 */
@Service
public class UserService {

    public User saveUser(User user){
        int id= new Random().nextInt(100);
        user.setId(Long.valueOf(id));
        return user;
    }
}

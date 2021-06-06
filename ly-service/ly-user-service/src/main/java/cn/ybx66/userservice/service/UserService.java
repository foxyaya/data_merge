package cn.ybx66.userservice.service;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.pojo.User;
import cn.ybx66.userservice.dto.UserReqDTO;
import cn.ybx66.userservice.dto.userDTO;

import java.sql.SQLException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 13:45
 * @description
 */
public interface UserService {

    String saveUser(userDTO user);

    int update(User user);

    int updateById(User user);

    User getUser(String username);

    User getUser(String username,String password);

    //弃用
    ResultMessageDTO getUserAll();

    ResultMessageDTO getUserInfo(String userId);

    ResultMessageDTO deleteUser(String userId);

    ResultMessageDTO getUserByUserOrPhone(UserReqDTO userReqDTO);

}

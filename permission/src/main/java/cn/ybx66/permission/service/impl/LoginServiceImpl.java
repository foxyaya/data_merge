package cn.ybx66.shiro.service.impl;


import cn.ybx66.shiro.service.LoginService;
import cn.ybx66.userapi.feigin.UserFigin;
import cn.ybx66.userapi.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserFigin userFigin;

    @Override
    public User getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }

    /**
     * 通过feigin在数据库查询用户信息
     *
     * @param userName 用户名
     * @return User
     */
    private User getMapByName(String userName) {
        // 使用feigin获取用户及其操作权限
        ResponseEntity<User> entity = userFigin.getUser(userName);
        User userInfo = entity.getBody();
        return userInfo;
    }
}

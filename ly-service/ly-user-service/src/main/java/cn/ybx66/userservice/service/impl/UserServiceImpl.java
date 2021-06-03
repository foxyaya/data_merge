package cn.ybx66.userservice.service.impl;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.conmmon.utils.Md5Utils;
import cn.ybx66.userapi.pojo.Permissions;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userapi.pojo.User;
import cn.ybx66.userservice.mapper.PermissionsMapper;
import cn.ybx66.userservice.mapper.RoleMapper;
import cn.ybx66.userservice.mapper.UserMapper;
import cn.ybx66.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static cn.ybx66.conmmon.utils.Md5Utils.getMd5CodeAndSalt;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 13:46
 * @description 用户业务处理
 */
@Service
public class UserServiceImpl implements UserService {

    private final static String SALT = "jh520";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionsMapper permissionsMapper;

    /**
     * 存放数据
     * @param user
     * @return String
     */
    @Override
    public String saveUser(User user) {
        User checkUser = this.getUser(user.getUserName());
        if (checkUser!=null){
           return null;
        }
        //todo 采用uuid设置为用户id
        user.setPassword(Md5Utils.getMd5Code(user.getPassword()));
        Integer status= userMapper.insert(user);
        return status.toString();
    }

    @Override
    public int update(User user) {
        User checkUser = this.getUser(user.getUserName());
        if (checkUser==null){
            return 0;
        }
        user.setId(checkUser.getId());
        user.setPassword(getMd5CodeAndSalt(user.getPassword()));
        int status = userMapper.updateByPrimaryKey(user);
        return status;
    }

    /**
     * 查询用户是否存在
     * @param userName
     * @return
     */
    @Override
    public User getUser(String userName) {
        User user = new User();
        user.setUserName(userName);
        List<User> userList = userMapper.select(user);
        if (userList==null || userList.size()==0){
            return null;
        }
        return getUserInfoALL(userList.get(0));
    }

    /**
     * Login登陆
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User getUser(String userName, String password) {
        User checkUser = this.getUser(userName);
        if (checkUser!=null){
            return new User("1",null,null,null);
        }
        User user = new User();
        user.setUserName(userName);
        List<User> userList = userMapper.selectByExample(user);
        //md5+盐 加密对比
        String code = Md5Utils.getMd5Code(password);
        if (!code.equals(Md5Utils.getMd5Code(userList.get(0).getPassword()))){
            return new User("2",null,null,null);
        }
        User userInfo = userList.get(0);
        return getUserInfoALL(userInfo);
    }

    /**
     * 查询出用户的角色及其权限
     * @param user
     * @return
     */
    public User getUserInfoALL(User user){
        //通过用户id获取角色
        Role role = new Role();
        role.setUserId(user.getId());
        List<Role> roleList = roleMapper.select(role);
        for(Role setRole:roleList){
            //为用户添加权限
            Permissions permissions = new Permissions();
            permissions.setRoleId(setRole.getId());
            List<Permissions> permissionsList =  permissionsMapper.select(permissions);
            if (permissionsList==null||permissionsList.size()==0){
                throw new LyException(ExceptionEnums.USER_PERMISSIONS_BE_NULL);
            }
            setRole.setPermissions(permissionsList);
        }
        user.setRoles(roleList);
        return user;
    }

}

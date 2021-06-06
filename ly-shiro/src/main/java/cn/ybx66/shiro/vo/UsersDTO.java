package cn.ybx66.shiro.vo;

import cn.ybx66.userapi.pojo.Role;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/23 18:39
 * @description 用户VO返回类
 */
@Data
public class UsersDTO implements Serializable {

    public String id;

    public String username;

    public String headImage;

    public String phone;

    public String sign;
    /**
     * 用户对应的角色集合
     */
    public String[] roles;

}

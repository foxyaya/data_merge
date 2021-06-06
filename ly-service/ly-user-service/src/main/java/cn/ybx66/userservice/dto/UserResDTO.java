package cn.ybx66.userservice.dto;

import cn.ybx66.userapi.pojo.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 1:34
 * @description
 */
@Data
public class UserResDTO implements Serializable {

    public String id;

    public String username;

    public String headImage;

    public String phone;

    public List<Role> roles;
}

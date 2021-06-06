package cn.ybx66.userservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 2:15
 * @description
 */
@Data
public class RoleDTO implements Serializable {

    public String id;

    public String[] roles;
}

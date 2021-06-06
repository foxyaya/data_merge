package cn.ybx66.userservice.service;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userservice.dto.RoleDTO;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/3/17 14:29
 * @description
 */
public interface RolesService {

    //插入角色信息
    String insertRoles(Role role);

    //更新角色信息
    int updateRoles(Role role);

    //更新角色信息
    ResultMessageDTO  updateRolesByuserId(RoleDTO roleDTO);



}

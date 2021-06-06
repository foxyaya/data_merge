package cn.ybx66.userservice.service.impl;

import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.pojo.Permissions;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userservice.dto.RoleDTO;
import cn.ybx66.userservice.mapper.PermissionsMapper;
import cn.ybx66.userservice.mapper.RoleMapper;
import cn.ybx66.userservice.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/3/17 14:30
 * @description 用户角色  暂不对用户权限控制在请求方法层次
 */
@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RoleMapper roleMapper;
//    @Autowired
//    private PermissionsMapper permissionsMapper;

    /**
     *新增角色信息和相应的权限功能
     * @param role
     * @return
     */
    @Override
    public String insertRoles(Role role) {
        roleMapper.insert(role);
        //取权限信息
//        List<Permissions> permissions = role.getPermissions();

        return role.getId();
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @Override
    public int updateRoles(Role role) {
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        //判断用户是否存在
        if (temp==null){
            return 0;
        }
        roleMapper.updateByPrimaryKey(role);
        return 1;
    }

    @Override
    public ResultMessageDTO updateRolesByuserId(RoleDTO roleDTO) {
        Role role = new Role();
        role.setFlag(1);
        role.setUserId(roleDTO.getId());
        List<Role> select = roleMapper.select(role);
        //先删除
        for (int i = 0; i < select.size(); i++) {
            roleMapper.deleteByPrimaryKey(select.get(i).getId());
        }
        //新增
        String[] roles = roleDTO.getRoles();
        if (roles!=null && roles.length!=0){
            for (int i = 0; i < roles.length; i++) {
                Role temp = new Role();
                try {
                    BeanUtil.autoSetAttrOnInsert(temp);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                temp.setUserId(roleDTO.getId());
                temp.setRoleName(roles[i]);
                roleMapper.insert(temp);
            }
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,"更新角色成功");
    }

}

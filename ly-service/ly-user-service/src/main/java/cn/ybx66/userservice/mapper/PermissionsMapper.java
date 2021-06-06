package cn.ybx66.userservice.mapper;

import cn.ybx66.userapi.pojo.Permissions;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/7 19:03
 * @description 权限
 */
@Repository
public interface PermissionsMapper extends Mapper<Permissions> {

}

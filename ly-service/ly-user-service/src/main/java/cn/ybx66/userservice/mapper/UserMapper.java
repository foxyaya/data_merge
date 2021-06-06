package cn.ybx66.userservice.mapper;

import cn.ybx66.userapi.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 9:50
 * @description
 */
@Repository
public interface UserMapper extends Mapper<User> {



}

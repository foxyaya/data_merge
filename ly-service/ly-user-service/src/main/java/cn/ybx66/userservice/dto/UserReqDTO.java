package cn.ybx66.userservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 1:52
 * @description 根据前端参数设置请求DTO
 */
@Data
public class UserReqDTO implements Serializable {

    public int page;

    public int limit;

    public String username;

    public String phone;
}

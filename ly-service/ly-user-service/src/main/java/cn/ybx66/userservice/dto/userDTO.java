package cn.ybx66.userservice.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/17 23:18
 * @description
 */
@Data
public class userDTO implements Serializable {

    public String username;

    public String password;

    public String phone;

    public String message;

    public String ip;

}

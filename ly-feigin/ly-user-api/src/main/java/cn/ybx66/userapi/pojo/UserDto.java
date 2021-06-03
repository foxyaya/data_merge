package cn.ybx66.permission.dto;

import cn.ybx66.userapi.pojo.Permissions;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户对象
 */
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = -9077975168976887742L;

    private String username;
    private String password;
    private String encryptPwd;
    private Long userId;
    private String salt;
    private List<String> roles;
	private List<Permissions> permissions;

}

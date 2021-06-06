package cn.ybx66.conmmon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/21 12:22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    USER_CANNOT_BE_NULL(400,"用户不能为空"),
    USER_BE_NULL(400,"用户名不存在"),
    PASSWORD_ERROR(400,"密码错误"),
    USER_PERMISSIONS_BE_NULL(400,"用户角色为空"),
    USER_UPDATE_ERROR(400,"用户更新失败"),
    TOKEN_IS_ERROR(400,"token无效"),
    ID_IS_ERROR(400,"id设置异常"),
    TIME_IS_ERROR(400,"时间设置异常"),
    FLAG_IS_ERROR(400,"逻辑位异常"),
    ERROR(400,"配置异常")
    ;
    private int code;
    private String msg;
}

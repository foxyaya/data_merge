package cn.ybx66.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/10 22:30
 * @description
 */
public class UserToken implements AuthenticationToken {
    private String token;
    public UserToken(String token){
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

package cn.ybx66.shiro.shiro;


import cn.ybx66.shiro.credentialsMatcher.CustomCredentialsMatcher;
import cn.ybx66.shiro.service.LoginService;
import cn.ybx66.userapi.pojo.Permissions;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userapi.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;
    /**
     * @MethodName doGetAuthorizationInfo
     * @Description 权限配置类
     * @Param [principalCollection]
     * @Return AuthorizationInfo
     * @Author Fox
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        //查询用户名称
        User user = loginService.getUserByName(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            //添加权限
//            for (Permissions permissions : role.getPermissions()) {
//                simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
//            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * @MethodName doGetAuthenticationInfo
     * @Description 认证配置类
     * @Param [authenticationToken]
     * @Return AuthenticationInfo
     * @Author Fox
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        //获取用户信息
        System.out.println("authenticationToken.getCredentials() = " + authenticationToken.getCredentials());
        String name = authenticationToken.getPrincipal().toString();
        User user = loginService.getUserByName(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, user.getPassword().toString(), getName());
            return simpleAuthenticationInfo;
        }
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //自定义认证加密方式
        CustomCredentialsMatcher customCredentialsMatcher = new CustomCredentialsMatcher();
        // 设置自定义认证加密方式
        super.setCredentialsMatcher(customCredentialsMatcher);
    }


}

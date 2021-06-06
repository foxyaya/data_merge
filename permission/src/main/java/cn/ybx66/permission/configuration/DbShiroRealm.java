package cn.ybx66.permission.configuration;

import java.util.List;

import cn.ybx66.permission.service.UserService;
import cn.ybx66.userapi.pojo.Permissions;
import cn.ybx66.userapi.pojo.UserDto;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DbShiroRealm extends AuthorizingRealm {
	private final Logger log = LoggerFactory.getLogger(DbShiroRealm.class);
	
	private static final String encryptSalt = "jh520";
	private UserService userService;
	
	public DbShiroRealm(UserService userService) {
		this.userService = userService;
		this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
	}
	
	@Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
		String username = userpasswordToken.getUsername();
		UserDto user = userService.getUserInfo(username);
		if(user == null)
			throw new AuthenticationException("用户名或者密码错误");
		
		return new SimpleAuthenticationInfo(user, user.getEncryptPwd(), ByteSource.Util.bytes(encryptSalt), "dbRealm");
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {      
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserDto user = (UserDto) principals.getPrimaryPrincipal();
        List<String> roles = user.getRoles();
        if(roles == null) {
            roles = userService.getUserRoles(user.getUsername());
            user.setRoles(roles);
        }
        if (roles != null)
            simpleAuthorizationInfo.addRoles(roles);
		//添加权限
		List<List<Permissions>> permission = userService.getPermission(user.getUsername());
		if( permission!=null){
			for (int i =0;i<permission.size();i++){
				for (Permissions permissions : permission.get(i)) {
					simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
				}
			}

		}
        return simpleAuthorizationInfo;
	}

	
}

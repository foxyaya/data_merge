package cn.ybx66.permission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ybx66.permission.configuration.JwtUtils;
import cn.ybx66.userapi.pojo.Permissions;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userapi.pojo.User;
import cn.ybx66.userapi.pojo.UserDto;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;



/**
 * 用户信息接口
 */
@Service
public class UserService {
	
	private static final String encryptSalt = "jh520";

	@Autowired
	private LoginService loginService;

	@Autowired
	private StringRedisTemplate redisTemplate;
   
    /**
     * 保存user登录信息，返回token
     * @param
     */
    public String generateJwtToken(String username) {
    	String salt = JwtUtils.generateSalt();
		redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);//保存在缓存中
    	return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }
    
    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    public UserDto getJwtTokenInfo(String username) {
    	String salt = "";
		salt = redisTemplate.opsForValue().get("token:"+username);
    	UserDto user = getUserInfo(username);
    	user.setSalt(salt);
    	return user;
    }

    /**
     * 清除token信息 下线就删除
     * @param
     * @param
	 *
     */
    public void deleteLoginInfo(String username) {

		redisTemplate.delete("token:"+username);
    }
    
    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    public UserDto getUserInfo(String userName) {
		User userByName = loginService.getUserByName(userName);
		UserDto user = new UserDto();
    	user.setUserId(Long.valueOf(userByName.getId()));
    	user.setUsername(userByName.getUsername());
    	user.setEncryptPwd(new Sha256Hash(userByName.getPassword(), encryptSalt).toHex());
    	return user;
    }
    
    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    public List<String> getUserRoles(String userId){
		List<String> list = new ArrayList<>();
		User userByName = loginService.getUserByName(userId);
		List<Role> roles = userByName.getRoles();
		for (Role role : roles){
			list.add(role.getRoleName());
		}
//		redisTemplate.opsForHash().put("Role:"+userId, "Role:"+userId, list);
		return list;
    }

	public List<List<Permissions>> getPermission(String userId){
		List<List<Permissions>> list = new ArrayList<>();
		User userByName = loginService.getUserByName(userId);
		List<Role> roles = userByName.getRoles();
		for (Role role : roles){
			list.add(role.getPermissions());
		}
//		redisTemplate.opsForHash().put("permissions:"+userId, "permissions:"+userId, list);
		return list;
	}

}

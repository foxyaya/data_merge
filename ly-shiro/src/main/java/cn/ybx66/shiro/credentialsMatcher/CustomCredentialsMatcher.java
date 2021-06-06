package cn.ybx66.shiro.credentialsMatcher;

import cn.ybx66.conmmon.utils.Md5Utils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/7 22:09
 * @description  用户验证匹配器 用户密码到达shiro中 转换为数据库存储的md5加密形式密码
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    private final static String SALT="jh520";

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        System.out.println("获取的明文密码：" + new String(token.getPassword()));
        Object tokenCredentials =Md5Utils.getMd5Code(Md5Utils.getMd5CodeAndSalt(new String(token.getPassword())));
//        Object tokenCredentials = new SimpleHash("md5", token.getPassword(), SALT, 2).toHex();
        //获取数据库存储密码
        Object accountCredentials = getCredentials(info);
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        return equals(tokenCredentials, accountCredentials);
    }
}

package cn.ybx66.conmmon.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 14:23
 * @description md5加密
 */
public class Md5Utils {
    private static final String SALT ="jh520";
    //输入明文密码
    public static String  getMd5Code(String password){
        StringBuilder sb = null;
        try {
            //创建加密对象
            //参数1: 算法名字
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //进行加密  返回加密之后结果也是字节
            byte[] digest = messageDigest.digest(password.getBytes());
            sb = new StringBuilder();
            for (byte b : digest) {
                //位运算
                int len = b & 0xff;    //0  0x0 0x1 0x2 0x3 0x4 0x9  10  0xa   15  0xf  16 0x10 170x11
                if(len<=15){
                    sb.append("0");
                }
                sb.append(Integer.toHexString(len));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static String getMd5CodeAndSalt(String password){
        return getMd5Code(getMd5Code(password+SALT));
    }
    //生成随机的盐
    public static  String  getSalt(int n){
        char[] code =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(code[new Random().nextInt(code.length)]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        //第一种
        String password = DigestUtils.md5DigestAsHex(("123456"+"jh520").getBytes());//e10adc3949ba59abbe56e057f20f883e
        password =DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("password = " + password);//ac12cb4ad5e726070130484dc6942488
        //第二种
        System.out.println(getMd5Code(Md5Utils.getMd5CodeAndSalt("123456789")));//3ace034425e4ba4f96e867b274b5b20f
    }

}

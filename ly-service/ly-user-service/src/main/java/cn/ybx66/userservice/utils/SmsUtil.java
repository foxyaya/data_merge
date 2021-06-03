package comybx.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/4/11 20:38
 * @description
 */
@Slf4j
@Component
public class SmsUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String YOUR_PHONE_KEY = "sms:phone:";

    static  volatile String verifyCode =null;

    public  void sendSms(String phone)  {
        try {
            String key = YOUR_PHONE_KEY+phone;
            JSONObject json = null;
            //生成6位验证码
             verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //发送短信
            ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "104339","653256ae-57b6-405f-a44d-10b282bfa065");
            Map<String, String> params = new HashMap<String, String>();
//            System.out.println("phone = " + phone);
            params.put("number",phone);
            params.put("message","您的验证码为:" + verifyCode + "，有效期为5分钟，邦邦商城！");
            if (verifyCode==null){
                log.info("验证码有问题");
            }
            String result = client.send(params);
            json = JSONObject.parseObject(result);
            System.out.println("result = " + result);
            if(json.getIntValue("code") != 0) {//发送短信失败 发送成功返回0
                log.error("短信发送失败！手机号码{}，状态为{}",phone,json.getIntValue("code"));
                return ;
            }
            //使用 redis记录时间 第二种 redis
            redisTemplate.opsForValue().set(key,verifyCode,5, TimeUnit.MINUTES);
            log.info("发送短信成功，验证码为：{}", verifyCode);
        }catch (Exception e){
            log.error("短信发送异常！手机号码{}",phone,e);
            return;
        }
    }
}

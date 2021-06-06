package cn.ybx66.userservice.controller;

import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/8 16:47
 * @description
 */
@RestController
@RequestMapping("/send")
public class SendController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String YOUR_PHONE_KEY = "sms:phone:";
    static final String YOUR_MAIL_KEY = "mail:num:";


    //ok
    @GetMapping("/sendMessage")
    @ResponseBody
    public ResultMessageDTO sendMessage(@RequestParam String phone) throws Exception {//HttpServletRequest request,
        String key = YOUR_PHONE_KEY+YOUR_PHONE_KEY+phone;
        //按手机号码进行限流
        String lastTime = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(lastTime)){
            Long aLong = Long.valueOf(lastTime);
            if (System.currentTimeMillis()-aLong<60000){
                //一分钟一条
                return new ResultMessageDTO(400, ResultDescDTO.FAIL,"一分钟一条");
            }
        }

        //准备利用mq发送的数据
        Map<String,String> msg=new HashMap<>();
        msg.put("phone",phone);

        //
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
        //避免干扰其他用户的注册  第一种 session方法
//        HttpSession session = request.getSession();
//        json = new JSONObject();
//        json.put("verifyCode"+userInfoDto.getUsername(), verifyCode);
//        json.put("createTime"+userInfoDto.getUsername(), System.currentTimeMillis());
//        // 将认证码存入SESSION 设置存活时间
//        session.setMaxInactiveInterval(5*60);
//        session.setAttribute("verifyCode"+userInfoDto.getUsername(), json);

        //使用 redis记录时间 第二种 redis
        //防止重复发送
        redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1, TimeUnit.MINUTES);
//        log.info("发送短信成功，手机号码：{}",phone);
//        log.info("继续执行");
         return new ResultMessageDTO(200, ResultDescDTO.OK,"success");
    }

    @GetMapping("/sendMail")
    public ResultMessageDTO sendMail(@RequestParam String mailNum){
        String key = YOUR_MAIL_KEY+mailNum;
        //TODO 按手机号码进行限流
        String lastTime = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(lastTime)){
            Long aLong = Long.valueOf(lastTime);
            if (System.currentTimeMillis()-aLong<60000){
                //一分钟一条
                return new ResultMessageDTO(400, ResultDescDTO.FAIL,"一分钟一条");
            }
        }

        //准备利用mq发送的数据
        Map<String,String> msg=new HashMap<>();
        msg.put("mailNum",mailNum);

        //
        amqpTemplate.convertAndSend("ly.smtp.exchange","smtp.verify.code",msg);

        redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1,TimeUnit.MINUTES);

         return new ResultMessageDTO(200, ResultDescDTO.OK,"success");
    }

}

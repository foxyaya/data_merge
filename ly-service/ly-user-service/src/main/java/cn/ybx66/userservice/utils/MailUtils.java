package cn.ybx66.userservice.utils;


import cn.ybx66.userservice.dto.SmtpDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/9/23 15:01
 * @description 邮箱
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmtpDTO.class)
public class MailUtils {

    @Autowired
    public AmqpTemplate amqpTemplate;

    @Autowired
    public SmtpDTO smtpDTO;

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 发送邮件的方法
     * @param to   ：收件人
     */
    public  void sendMail(String to){
        //1.获得Session对象
        Properties props=new Properties();
        props.put("mail.transport.protocol","smtp");//连接协议
        props.put("mail.smtp.host","smtp.qq.com");//主机名
        props.put("mail.smtp.port","465");//端口号
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable","true");//设置是否使用ssl安全连接，一般都使用
        props.put("mail.debug","true");//设置是否显示debug信息 true会在控制台显示相关信息
        //得到会话对象
        Session session=Session.getInstance(props);
        //获取邮件对象
        Message message=new MimeMessage(session);
        try {
            //生成6位验证码
            String code = String.valueOf(new Random().nextInt(899999) + 100000);
            //设置发件人地址
            message.setFrom(new InternetAddress("917367646@qq.com"));

            //设置收件人地址 (将参数传进来)
            message.setRecipients(Message.RecipientType.TO,new InternetAddress[]{new InternetAddress(to)});
            //设置邮件标题
            message.setSubject("来自Bang的激活邮件");
            //设置邮件正文
            message.setContent("<h1>Bang！亲~我来啦！</h1><h3>您的验证码为："+code+"，请在五分钟之内使用！</h3>"+"<p align=right>系统自动发送，请不要回复！</p>","text/html;charset=UTF-8");
            //得到邮差对象
            Transport transport=session.getTransport();
            //连接自己的邮箱账户
            transport.connect(smtpDTO.getUser(),smtpDTO.getAuthor());//密码为授权码
            //发送邮件
            transport.sendMessage(message,message.getAllRecipients());

            //发送成功需要保存在redis中
            redisTemplate.opsForValue().set(to,code,5, TimeUnit.MINUTES);
            log.info("您的验证码为：{}",code);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

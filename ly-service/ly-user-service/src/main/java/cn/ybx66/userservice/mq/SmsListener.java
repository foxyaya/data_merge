package comybx.demo.mq;

import comybx.demo.utils.MailUtils;
import comybx.demo.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/4/11 21:45
 * @description
 */
@Component
@Slf4j
//@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private MailUtils mailUtils;

    /**
     * 发送验证码  使用其进行异步
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms.verify.code.queue",durable = "true"),
            exchange = @Exchange(name = "ly.sms.exchange", type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"
    ))
    //key = {"#.#"}  绑定一切路由
    public void listenInsertOrUpdate(Map<String,String> msg)  {
        if (CollectionUtils.isEmpty(msg)){
            return ;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)){
            return;
        }
        smsUtil.sendSms(phone);
    }
    //发送邮箱 通过消息队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "smtp.verify.code.queue",durable = "true"),
            exchange = @Exchange(name = "ly.smtp.exchange", type = ExchangeTypes.TOPIC),
            key = "smtp.verify.code"
    ))
    //key = {"#.#"}  绑定一切路由
    public void listenSMTP(Map<String,String> msg)  {
        if (CollectionUtils.isEmpty(msg)){
            return ;
        }
        String mailNum = msg.remove("mailNum");
        if (StringUtils.isBlank(mailNum)){
            return;
        }
        //进行邮箱认证消息的发送
        mailUtils.sendMail(mailNum);
    }



















}

package ybx66.configure;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/5/12 16:17
 * @description 微信支付
 */
@Configuration
public class WXPayConfiguration {
    /**
     * 配置参数加入SpringIOC容器 方便使用
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "ly.pay")
    public PayConfig payConfig(){
        return new PayConfig();
    }

    @Bean
    public WXPay wxPay(PayConfig payConfig){
        return new WXPay(payConfig, WXPayConstants.SignType.HMACSHA256);
    }
}

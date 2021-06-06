package ybx66.configure;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/5/12 15:50
 * @description微信支付
 */
@Data
public class PayConfig implements WXPayConfig {

    private String appID;//公众号id

    private String mchID;//商户id

    private String key;//生成签名的密钥

    private int httpConnectTimeoutMs;//生成签名的密钥

    private int httpReadTimeoutMs;//读取超时时间

    private String notifyUrl ;

    @Override
    public InputStream getCertStream() {
        return null;
    }

}

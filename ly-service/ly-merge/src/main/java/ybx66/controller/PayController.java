package cn.ybx66.controller;

import cn.ybx66.utils.payUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/27 21:41
 * @description 微信支付
 */
@RequestMapping("/pay")
@RestController
public class PayController {

    @Autowired
    private payUtil payUtil;

    /**
     * 微信支付生成订单
     * @param orderId
     */
    @PostMapping("/create")
    public String test(@RequestParam("orderId") Long orderId,@RequestParam("total") Long total,@RequestParam("dec") String dec){
        return payUtil.createPayUrl(orderId, total, dec);
    }

}

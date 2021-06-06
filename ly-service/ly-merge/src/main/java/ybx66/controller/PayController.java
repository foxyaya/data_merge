package ybx66.controller;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ybx66.utils.payUtil;

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
     * 生成微信支付url
     * @param
     */
    @PostMapping("/create")
    public ResultMessageDTO test(@RequestParam String shopId){//@RequestParam("orderId") Long orderId, @RequestParam("total") Long total, @RequestParam("dec") String dec
        return payUtil.createPayUrl(shopId);
    }
    //回调地址  Notify POST请求 @RequestMapping(value = "payNotifyUrl") 此系统暂不需要

    //主动 查询 异步通知也可以 但由于使用的是本地部署 就只整理了主动查询 用户体验感略感欠缺(最大的弊端 用户不进行查询，状态无法更新)
    //对于开户的主动更新 第一个采用的是webSocket,它的全双工模式很快乐，两端都能发送信息通知
    //第二个 前端对支付查询接口进行轮询  采取的是第二种
    @PostMapping("/queryPayStatus")
    public ResultMessageDTO queryPayStatus(@RequestParam String shopId,@RequestParam String orderId){//@RequestParam("orderId") Long orderId, @RequestParam("total") Long total, @RequestParam("dec") String dec
        return payUtil.queryPayStatus(orderId,shopId);
    }
}

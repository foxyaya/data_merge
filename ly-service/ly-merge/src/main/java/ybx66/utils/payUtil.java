package ybx66.utils;



import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Shop;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ybx66.configure.PayConfig;
import ybx66.dto.PayDTO;
import ybx66.mapper.ShopMapper;
import ybx66.service.ShopService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/5/12 16:28
 * @description
 */
@Slf4j
@Component
public class payUtil {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayConfig config;

    @Autowired
    public ShopService shopService;

    @Autowired
    public ShopMapper shopMapper;

    /**
     * 判断签名有效性
     * @param result
     */
    public void isVaildSign(Map<String,String> result) {
        //重新生成签名
        try {
            String s = WXPayUtil.generateSignature(result, config.getKey(), WXPayConstants.SignType.HMACSHA256);
            String s1 = WXPayUtil.generateSignature(result, config.getKey(), WXPayConstants.SignType.MD5);

            //和传过来的比较
            String sign = result.get("sign");
            if (!StringUtils.equals(s,sign) && !StringUtils.equals(s1,sign)){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断状态
     * @param result
     * @throws Exception
     */
    public void isSuccess(Map<String, String> result) throws Exception {
        String return_code = result.get("return_code");
        if ("FAIL".equals(return_code)) {
            log.error("[微信支付] 通信失败 原因:{}", result.get("return_msg"));
            throw new Exception();
        }
        String result_code = result.get("result_code");
        if ("FAIL".equals(result_code)) {
            log.error("[微信支付] 业务失败 原因:{}", result.get("err_code_des"));
            throw new Exception();
        }
    }

    /**
     * 生成微信支付订单    通知可以通过对订单状态->前端轮询  也可以使用websocket进行页面通知前端
     * @param
     * @param
     * @param
     * @return
     */
    public ResultMessageDTO createPayUrl(String shopId) {//Long orderId,Long totalPay,String dec
        if (shopId==null || shopId.equals("")){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"请先填写好店铺信息");
        }
        //这里可以采用redis进行订单号的保存 在别的地方取 有时候可以用redis生成的id标识某些数据 redis是单线程的 生成的id具有唯一性 也是防止重复
//        String key = "ly.pay.url." + orderId;
//        try {
//            String url = this.redisTemplate.opsForValue().get(key);
//            if (StringUtils.isBlank(url)) {
//                return url;
//            }
//        } catch (Exception e) {
//            log.error("查询缓存付款链接异常,订单编号：{}", orderId, e);
//        }
        String url="";
        PayDTO payDTO = new PayDTO();
        try {
            //为返回体装配属性
            Map<String, String> data = new HashMap<>();
            // 商品描述
            data.put("body", "开店");
            String orderId = String.valueOf(new Random().nextInt(899999999) + 1000000000);
            payDTO.setOrderId(orderId);
            // 订单号
            data.put("out_trade_no", orderId);
            //货币
            data.put("fee_type", "CNY");
            //金额，单位是分
            data.put("total_fee", "1");
            //调用微信支付的终端IP  (随便输入)
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", config.getNotifyUrl());
            // 交易类型为扫码支付 生成码被扫  还有个像付款码一样的
            data.put("trade_type", "NATIVE");
            //商品id,使用假数据   ----------------- 从别的地方调（订单）
            data.put("product_id", "1234567");
            //wxPay.unifiedOrder 封装了一部分重要参数
            Map<String, String> result = this.wxPay.unifiedOrder(data);
            //预检
            //数据校验 通信标志
            isSuccess(result);
//            if ("SUCCESS".equals(result.get("return_code"))) {
            url = result.get("code_url");
            String out_trade_no = result.get("prepay_id");
            log.info("获取到的url是：{}",url);
            // 将付款地址缓存，时间为10分钟
//                try {
//                    this.redisTemplate.opsForValue().set(key, url, 10, TimeUnit.MINUTES);
//                } catch (Exception e) {
//                    log.error("缓存付款链接异常,订单编号：{}", orderId, e);
//                }
//                return url;

            //在此处调用二维码生成工具生成付款二维码 保留在服务器上
            //此处解决办法是 前端调用qrcode生成付款码
//            File file = new File("C://Users/ACER/Desktop/表情包",out_trade_no+".jpg");
//            if (!file.exists()){
//                file.createNewFile();
//            }
//            QRCodeUtil.createImage(url, new FileOutputStream(file));
        } catch (Exception e) {
            log.error("创建预交易订单异常", e);
            return null;
        }
        payDTO.setUrl(url);
        return new ResultMessageDTO(200, ResultDescDTO.OK,payDTO);
    }

    /**
     * 主动查询订单状态
     * @param OrderId
     */
    public ResultMessageDTO queryPayStatus(String OrderId,String shopId){
        if (OrderId==null || OrderId.equals("") || shopId==null || shopId.equals("")){
            return new ResultMessageDTO(200,ResultDescDTO.OK,"请先填写好店铺信息");
        }
        try {
            Map<String,String> data = new HashMap<>();
            data.put("out_trade_no",OrderId);
            Map<String, String> result = wxPay.orderQuery(data);

            //校验状态
            //检验签名
            //校验金额
            //数据校验 通信标志
            isSuccess(result);
            //校验签名
            isVaildSign(result);
            //校验金额
            String totalFeeStr = result.get("total_fee");
            String out_trade_no = result.get("out_trade_no");
            if (StringUtils.isEmpty(totalFeeStr) || StringUtils.isEmpty(out_trade_no)){
                throw new Exception();
            }
            /**
             * SUCCESS—支付成功
             *
             * REFUND—转入退款
             *
             * NOTPAY—未支付
             *
             * CLOSED—已关闭
             *
             * REVOKED—已撤销（付款码支付）
             *
             * USERPAYING--用户支付中（付款码支付）
             *
             * PAYERROR--支付失败(其他原因，如银行返回失败)
             */
            //查询订单状态
            String tradeState = result.get("trade_state");
            if ("SUCCESS".equals(tradeState)){
                //支付成功
                //修改订单状态 返回成功
                ResultMessageDTO byId = shopService.findById(shopId);
                Shop message = (Shop) byId.getMessage();
                message.setFlag(1);
                shopMapper.updateByPrimaryKey(message);
                return new ResultMessageDTO(200,ResultDescDTO.OK,"支付成功");
            }

            if ("NOTPAY".equals(tradeState) || "USERPAYING".equals(tradeState)) {
                //返回未支付 这两种状态不一定是用户没有支付 可能是正好你查找的时间之内 用户支付的回调没有进行完
            }
            //返回支付失败

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,"支付失败");
    }

}

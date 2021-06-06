package ybx66.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/3 2:05
 * @description
 */
@Data
public class ShopResDTO implements Serializable {
    /**
     * id
     */
    public String id;

    public String userId;

    /**
     * 商店名称
     */
    public String shopName;

    /**
     * 商店图片
     */
    public String shopImage;

    /**
     * 商店说明
     */
    public String shopSign;

    /**
     * 逻辑位 店铺状况 0 无 1 在 2待支付
     */
    public Integer flag;

    /**
     * 所属用户
     */
    public String own;
}

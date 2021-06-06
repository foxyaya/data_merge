package ybx66.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/3 3:43
 * @description
 */
@Data
public class GoodsResDTO implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 商店id
     */
    private String shopId;

    private String typeId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品价格
     */
    private String goodsPrice;

    /**
     * 逻辑位
     */
    private Integer flag;

    private String shopName;
}

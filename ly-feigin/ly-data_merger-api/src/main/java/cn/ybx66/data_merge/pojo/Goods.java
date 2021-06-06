package cn.ybx66.data_merge.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 18:08
 * @description
 */
@Data
@Table(name = "tb_goods")
public class Goods implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 商店id
     */
    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "type_id")
    private String typeId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品图片
     */
    @Column(name = "goods_image")
    private String goodsImage;

    /**
     * 商品价格
     */
    @Column(name = "goods_price")
    private String goodsPrice;

    /**
     * 逻辑位
     */
    @Column(name = "flag")
    private Integer flag;

    /**
     * 时间
     */
    @Column(name = "last_time")
    private Date lastTime;
}

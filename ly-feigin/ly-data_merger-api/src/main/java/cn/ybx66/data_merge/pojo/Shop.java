package cn.ybx66.data_merge.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 18:07
 * @description
 */
@Data
@Table(name="tb_shop")
public class Shop implements Serializable {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "user_id")
    public String userId;

    /**
     * 商店名称
     */
    @Column(name = "shop_name")
    public String shopName;

    /**
     * 商店图片
     */
    @Column(name = "shop_image")
    public String shopImage;

    /**
     * 商店说明
     */
    @Column(name = "shop_sign")
    public String shopSign;

    /**
     * 逻辑位 店铺状况 0 无 1 在 2待支付
     */
    @Column(name = "flag")
    public Integer flag;

    /**
     * 时间
     */
    @Column(name = "last_time")
    public Date lastTime;




}

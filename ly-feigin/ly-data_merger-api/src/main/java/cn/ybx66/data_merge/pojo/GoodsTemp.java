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
 * @date 2021/4/28 18:08
 * @description
 */
@Data
@Table(name="tb_goods_temp")
public class GoodsTemp implements Serializable {

    /**
     *
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     *
     */
    @Column(name = "ids")
    private String ids;

    /**
     *
     */
    @Column(name = "price")
    private String price;

    /**
     *
     */
    @Column(name = "sign")
    private String sign;

    /**
     *
     */
    @Column(name = "flag")
    private Integer flag;

    /**
     *
     */
    @Column(name = "last_time")
    private Date lastTime;




}

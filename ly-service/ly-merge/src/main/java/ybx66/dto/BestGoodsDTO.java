package ybx66.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/7 17:14
 * @description
 */
@Data
public class BestGoodsDTO implements Serializable {

    private Integer limit;

    private String type;

    private String goodsName;

//    private String filed;
}

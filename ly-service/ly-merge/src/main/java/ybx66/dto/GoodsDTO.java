package ybx66.dto;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/3 3:05
 * @description
 */

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsDTO implements Serializable {

    public Integer page;

    public Integer limit;

    public String goodsName;

    public String goodsType;

    public String shopName;
}

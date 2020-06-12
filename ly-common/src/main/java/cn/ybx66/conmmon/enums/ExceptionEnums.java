package cn.ybx66.conmmon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/21 12:22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOND(404,"商品分类未找到"),
    BRAND_NOT_FOUND(404,"品牌没有找到"),
    BRAND_INSERT_ERROR(400,"品牌新增失败")
    ;
    private int code;
    private String msg;
}

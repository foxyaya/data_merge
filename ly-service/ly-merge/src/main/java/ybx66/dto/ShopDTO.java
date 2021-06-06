package ybx66.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 12:18
 * @description 请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO implements Serializable {

    public Integer page;

    public Integer limit;

    public String userId;

    public String shopName;
}

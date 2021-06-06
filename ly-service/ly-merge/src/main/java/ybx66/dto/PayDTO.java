package ybx66.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 23:33
 * @description
 */
@Data
public class PayDTO implements Serializable {

    public String orderId;

    public String url;
}

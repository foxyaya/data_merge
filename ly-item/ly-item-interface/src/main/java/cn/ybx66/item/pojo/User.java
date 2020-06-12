package cn.ybx66.item.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/20 22:48
 */
@Data
public class User implements Serializable {

    private Long id;
    private String name;
    private Long price;
}

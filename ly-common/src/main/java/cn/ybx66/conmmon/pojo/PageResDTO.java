package cn.ybx66.conmmon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 18:36
 * @description
 */
@Data
@AllArgsConstructor
public class PageResDTO implements Serializable {
    private int pageCount;
    private Object data;
}

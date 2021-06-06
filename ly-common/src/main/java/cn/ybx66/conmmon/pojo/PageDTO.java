package cn.ybx66.conmmon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 18:28
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO implements Serializable {

    public int page;

    public int limit;

    public String userId;
}

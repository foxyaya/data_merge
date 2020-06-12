package cn.ybx66.conmmon.vo;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import lombok.Data;


/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/21 12:32
 */
@Data
public class ExceptionResult {

    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}


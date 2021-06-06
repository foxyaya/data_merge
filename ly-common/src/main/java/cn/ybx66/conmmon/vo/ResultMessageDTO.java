package cn.ybx66.conmmon.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class ResultMessageDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer code;

    private ResultDescDTO desc;

    private Object message;

    public ResultMessageDTO(Integer code, ResultDescDTO desc, Object message) {
        this.code = code;
        this.desc = desc;
        this.message = message;
    }

}

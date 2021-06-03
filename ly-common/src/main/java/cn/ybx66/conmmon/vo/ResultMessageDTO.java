package comybx.demo.dto;

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

    /**
     * 此字段是为了适配微信支付，仅该模块存在
     */
    private String payDesc;

    private Object message;

    public ResultMessageDTO(Integer code, ResultDescDTO desc, Object message) {
        this.code = code;
        this.desc = desc;
        this.message = message;
    }

    public ResultMessageDTO(Integer code, String payDesc, Object message) {
        this.code = code;
        this.payDesc = payDesc;
        this.message = message;
    }
}

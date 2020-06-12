package cn.ybx66.conmmon.exception;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/21 12:20
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException{
    private ExceptionEnums exceptionEnums;
}

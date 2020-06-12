package cn.ybx66.conmmon.advice;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.conmmon.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/21 12:04
 */
@ControllerAdvice
public class CommonExceptionHandler {

    /**
     * 自定义异常处理通知-LyException
     * @param e
     * @return
     */
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        ExceptionEnums em = e.getExceptionEnums();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(e.getExceptionEnums()));
    }
}

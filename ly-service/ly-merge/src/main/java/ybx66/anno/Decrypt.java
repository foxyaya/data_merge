package ybx66.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/30 16:04
 * @description 需要使用到解密的时候加上
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {
}

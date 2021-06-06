package cn.ybx66.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/11/16 18:56
 * @description
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ly.smtp")
public class SmtpDTO {

    private String user;

    private String author;
}

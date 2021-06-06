package ybx66.configure;

import lombok.Data;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/6 13:13
 * @description
 */
@Data
public class JdbcConnectConfig {

    private String username;

    private String password;

    private String driverClassName;

    private String url;
}

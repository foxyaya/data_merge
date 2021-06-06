package ybx66.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/30 13:22
 * @description 保护数据安全 约定数据加密 salt
 */
@Data
public class JDBCDTO implements Serializable {

    public String url;

    private String username;

    private String password;

    private String ip;

    private String database;
}

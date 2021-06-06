package ybx66.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ybx66.configure.JdbcConnectConfig;
import ybx66.dto.JDBCDTO;

import java.sql.*;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/27 17:41
 * @description
 */
public class JDBCConnection {
    //得到数据库连接
    public Connection getConnection(String driverClassName,String username,String password,String url) throws SQLException {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url,username,password);

    }
    //todo 通过用户提交的数据来获取连接（整合不同数据库的数据）  默认mysql数据库
    public Connection getOtherConnection(String driverClassName,String username,String password,String ip) throws SQLException {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://"+ip+":3306/data_merge?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true",username,password);
    }
}

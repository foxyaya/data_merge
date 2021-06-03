package ybx66.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/27 17:41
 * @description
 */
@Configuration
public class JDBCConnection {
    @Value("${ly.jdbc.username}")
    private String USER ;
    @Value("${ly.jdbc.password}")
    private String PWD;
    @Value("${ly.jdbc.url}")
    private String URL;
    @Value("${ly.jdbc.driverClassName}")
    private  String DRIVER;
    //注册驱动

    //得到数据库连接
    public  Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL,USER,PWD);
    }
    //关闭连接 执行 的打开资源
    public static void close(Connection conn, Statement stmt){
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭所有的打开资源
    public static void close(Connection conn, Statement stmt, ResultSet rs){
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

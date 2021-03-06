package cn.ybx66.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 9:15
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient//启动时才会被eureka注册到
@MapperScan("cn.ybx66.userservice.mapper")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class);
    }
}

package cn.ybx66.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableFeignClients(basePackages={"cn.ybx66.userapi.feigin"})
public class UserApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class);
    }
}

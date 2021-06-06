package cn.ybx66.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/11 13:08
 * @description token+shiro+redis
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages={"cn.ybx66.userapi.feigin"})
public class PermissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class,args);
    }
}

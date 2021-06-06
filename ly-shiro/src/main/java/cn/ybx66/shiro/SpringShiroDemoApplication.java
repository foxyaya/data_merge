package cn.ybx66.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * redis+cookies+shiro
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages={"cn.ybx66.userapi.feigin"})
public class SpringShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShiroDemoApplication.class, args);
    }
}

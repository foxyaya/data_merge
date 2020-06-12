package cn.ybx66;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/20 16:53
 */
@SpringBootApplication
@EnableDiscoveryClient//启动时才会被eureka注册到
@MapperScan("cn.ybx66.item.mapper")
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class);
    }
}

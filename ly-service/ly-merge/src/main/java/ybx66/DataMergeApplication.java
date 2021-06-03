package ybx66;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 15:24
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.ybx66.mapper")
public class DataMergeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataMergeApplication.class,args);
    }
}

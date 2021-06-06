package cn.ybx66;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/6 18:37
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages={"cn.ybx66.data_merge.feigin"})
public class LyUploadApplication  {
    public static void main(String[] args) {
        SpringApplication.run(LyUploadApplication.class,args);
    }

}

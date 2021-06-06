package cn.ybx66.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/20 16:11
 */
@EnableZuulProxy
@SpringCloudApplication
public class LyGateway {

    public static void main(String[] args) {
        SpringApplication.run(LyGateway.class);
    }
}
